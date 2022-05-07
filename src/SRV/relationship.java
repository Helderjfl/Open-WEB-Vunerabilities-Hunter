package SRV;

import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 * 
 * @author Helder
 */
public class relationship {

    private LinkedList<String> linesToAnalyze = new LinkedList();
    private LinkedList<String> variablesOfSql = new LinkedList();
    private LinkedList<String> occurrencesOfScopes = new LinkedList();
    private LinkedList<String> sendToTheModule = new LinkedList();
    private LinkedList<String> sendToTheModuleMethod = new LinkedList();
    private LinkedList<String> methodsOfSql = new LinkedList();
    private LinkedList<String> storesLineNumbers = new LinkedList();

    /**
     * 
     * @return Return the values that will be analysed by the module.
     */
    public LinkedList<String> getSendToTheModuleMethod() {
        return sendToTheModuleMethod;
    }

    /**
     * Set the values that should be analysed by the module (but it'll pass for some filters before that)
     * @param sendToTheModuleMethod
     */
    public void setSendToTheModuleMethod(LinkedList<String> sendToTheModuleMethod) {
        this.sendToTheModuleMethod = sendToTheModuleMethod;
    }

    /*
    public void locatedSQL(String code, LinkedList identifiers) {
        int found = -1;
        int openQuote = -1;
        int closeQuote = -1;
        int semicolon = -1;
        int firstOccurrence = 999999999;
        boolean locatesQuotes = false;
        boolean locatesSemicolon = false;
        boolean hasSQL = true;


        String sqlTreatment = "";
        while (hasSQL) {
            firstOccurrence = 999999999;
            for (int i = 0; i < identifiers.size(); i++) {
                found = code.indexOf((String) identifiers.get(i));
                if (found != -1 && found < firstOccurrence) {
                    firstOccurrence = found;
                }

            }
            if (firstOccurrence != 999999999) {
                found = firstOccurrence;
            } else {
                hasSQL = false;
            }
            if (found != -1) {
                locatesQuotes = false;
                while (!locatesQuotes) {
                    openQuote = code.indexOf("\"");
                    code = code.replaceFirst("\"", " ");
                    closeQuote = code.indexOf("\"");
                    if (openQuote != -1 && closeQuote != -1) {
                        //////procura o comando que está entre as Aspas que se refere ao comando SQL
                        if (openQuote < found && found < closeQuote) {
                            locatesSemicolon = false;
                            //////procura o ; referente a linha do comando SQL
                            while (!locatesSemicolon) {
                                semicolon = code.indexOf(";");
                                if (semicolon > found || semicolon == -1) {
                                    locatesSemicolon = true;
                                } else {
                                    code = code.replaceFirst(";", " ");
                                }
                            }
                            //JOptionPane.showMessageDialog(null, code);
                            sqlTreatment = code.substring(openQuote, semicolon + 1);
                            //////copia para o code somente parte do código onde não foi feito a busca por comandos SQL ainda
                            code = code.substring(semicolon + 1);
                            //////corrige a falta da primeira Aspa
                            sqlTreatment = sqlTreatment.replaceFirst(" ", "\"");
                            linesToAnalyze.addLast(sqlTreatment);
                            //JOptionPane.showMessageDialog(null, sqlTreatment);
                            locatesQuotes = true;
                            // remove aspas ate encontrar a sql, depois ela é removida e reconstruida
                            //e adicionada na lista para analises
                        } else {
                            code = code.replaceFirst("\"", " ");
                        }
                    } else {
                        locatesQuotes = true;
                    }
                }
            }

        }

    }*/
    
    /**
     * Find the SQL commands used on the code.
     * @param code
     * @param identifiers
     */
    public void locatedSQL(String code, LinkedList identifiers) {
        int firstQuote = 0;
        int lastQuote = -1;
        int semicolon = -1;
        String aux = "";
        boolean locatesSemicolon = false;
        
        while (firstQuote != -1){
            firstQuote = code.indexOf("\"");
            if (firstQuote != -1){
                code = code.replaceFirst("\"", "@quotemark@");
                lastQuote = code.indexOf("\"");
                if (lastQuote != -1){
                    aux = code.substring(firstQuote, lastQuote);
                    for (int i = 0;i < identifiers.size();i++){
                        if (aux.contains(identifiers.get(i).toString())){
                            while (!locatesSemicolon) {
                                semicolon = code.indexOf(";");
                                if (semicolon > firstQuote || semicolon == -1) {
                                    locatesSemicolon = true;
                                } else {
                                    code = code.replaceFirst(";", " ");
                                }
                            }
                            aux = code.substring(firstQuote, semicolon+1);
                            aux = aux.replaceFirst("@quotemark@", "\"");
                            linesToAnalyze.addLast(aux);
                            code = code.substring(semicolon+1);
                            semicolon = -1;
                            //locatesSemicolon = false;
                        }
                    }
                    if (locatesSemicolon)
                        locatesSemicolon = false;
                    else
                        code = code.replaceFirst("\"", "@quotemark@");
                }
            }
        }
    }
    /////////////////////////

    /**
     * 
     * @return Return the lines that have to be analysed
     */
    public LinkedList<String> getLinesToAnalyze() {
        return linesToAnalyze;
    }

    /**
     * 
     * @param linesToAnalyze
     */
    public void setLinesToAnalyze(LinkedList<String> linesToAnalyze) {
        this.linesToAnalyze = linesToAnalyze;
    }

    /**
     * Find the variables used at the SQL commands.
     * @param variables
     */
    public void locatesVariablesInSql(identification variables) {
        boolean ended = false;
        int opensMore = -1;
        int closeMore = -1;
        String line = "";
        int semicolon = -1;
        String valid = "";
        for (int i = 0; i < linesToAnalyze.size(); i++) {
            ended = false;
            line = linesToAnalyze.get(i);
            line = line.concat(";");

            while (!ended) {
                opensMore = line.indexOf("+");
                if (opensMore != -1) {
                    line = line.replaceFirst("\\+", " ");
                    closeMore = line.indexOf("+");
                    semicolon = line.indexOf(";");
                    if (closeMore != -1 && closeMore < semicolon) {
                        valid = line.substring(opensMore, closeMore);
                        //////evita que o que foi pego seja algo dentro do comando SQL dentro das Aspas em si
                        if (valid.indexOf("\"") == -1) {
                            String identify = line.substring(opensMore, closeMore);
                            identify = filterControls(identify);
                            //////verifica que no identify há apenas a variável ou uma função que retorna um resultado
                            if (validTokenOfSQL(identify)) {
                                variablesOfSql.addLast(line.substring(opensMore, closeMore));
                                line = line.replaceFirst("\\+", " ");
                            } else {
                                //////pega somente o nome do método
                                methodsOfSql.addLast(identify.substring(0, identify.indexOf("(")));
                                System.out.println("AQUI1" + methodsOfSql.getLast());
                                //////pega a estrutura completa da declaração do método
                                methodsOfSql.addLast(identify.substring(0, identify.indexOf(")") + 1));
                                System.out.println("AQUI2" + methodsOfSql.getLast());
                                //Aqui eu armazeno na lista o nome do metodo e a sua estrutura inteira
                                //////encontra as variáveis no método
                                String variablesOnMethod[] = foundVariablesOnMethod(methodsOfSql.getLast());
                                if (variablesOnMethod.length > 0) {
                                    for (int j = 0; j < variablesOnMethod.length; j++) {
                                        variablesOfSql.addLast(variablesOnMethod[j]);
                                    }
                                }
                                line = line.replaceFirst("\\+", " ");
                            }
                        }
                    } else {//valida declaracao no final da sql onde nao possui +Estrutra+
                        valid = line.substring(opensMore, semicolon);
                        if (valid.indexOf("\"") == -1) {
                            String identify = line.substring(opensMore, semicolon);
                            identify = filterControls(identify);
                            if (validTokenOfSQL(identify)) {//verifica se é metodo ou variavel
                                variablesOfSql.addLast(line.substring(opensMore, semicolon));
                            } else {
                                methodsOfSql.addLast(identify.substring(0, identify.indexOf("(")));
                                System.out.println("AQUI1" + methodsOfSql.getLast());
                                methodsOfSql.addLast(identify.substring(0, identify.indexOf(")") + 1));
                                System.out.println("AQUI2" + methodsOfSql.getLast());
                                String variablesOnMethod[] = foundVariablesOnMethod(methodsOfSql.getLast());
                                if (variablesOnMethod.length > 0) {
                                    for (int j = 0; j < variablesOnMethod.length; j++) {
                                        variablesOfSql.addLast(variablesOnMethod[j]);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ended = true;
                }
            }
        }

    }

    /**
     * 
     * @return Return the variables used at the SQL commands.
     */
    public LinkedList<String> getVariablesOfSql() {
        return variablesOfSql;
    }

    /**
     * Set que variables that were used at the SQL commands.
     * @param variablesAndMethodsOfSql
     */
    public void setVariablesOfSql(LinkedList<String> variablesAndMethodsOfSql) {
        this.variablesOfSql = variablesAndMethodsOfSql;
    }

    /**
     * 
     * @param code
     * @param codeForEachScope
     * @param hierarchy
     * @param listOfVariables
     * @param namesOfMehods
     */
    public void storageHierarchyOccurrences(String code, LinkedList<String> codeForEachScope, LinkedList<String> hierarchy, LinkedList<String> listOfVariables, LinkedList<String> namesOfMehods) {
        String scope = "";
        if (linesToAnalyze.size() > 0) {
            for (int k = 0; k < linesToAnalyze.size(); k++) {
                String scopeFound = linesToAnalyze.get(k);

                int valid = -1;
                int timesThatSpentByTheScope = 0;
                int posFound = 0;
                for (int i = 0; i < codeForEachScope.size(); i++) {
                    valid = codeForEachScope.get(i).indexOf(scopeFound);
                    if (valid != -1) {
                        //escopo= conteudoEscopo.get(i).substring(conteudoEscopo.get(i).indexOf("H"));
                        //////pega o identificador do escopo onde se encontra o comando SQL
                        scope = codeForEachScope.get(i).substring(codeForEachScope.get(i).indexOf("H"), codeForEachScope.get(i).indexOf("]"));
                        occurrencesOfScopes.addLast(scope);
                        //JOptionPane.showMessageDialog(null, scope);
                        posFound = i;
                    }
                }
                for (int i = 0; i <= posFound; i++) {
                    valid = codeForEachScope.get(i).indexOf("[" + scope + "]");
                    if (valid != -1) {
                        timesThatSpentByTheScope++;
                    }
                }

                String occurrenceOfScope = "[" + occurrencesOfScopes.get(k).substring(occurrencesOfScopes.get(0).indexOf("]") + 1, occurrencesOfScopes.get(k).length()) + "]";
                LinkedList<String> scopesToExamine = new LinkedList();
                int passou = 0;
                //////busca quais escopos devem ser examinados (possuem código SQL)
                for (int i = 0; i < hierarchy.size(); i++) {
                    if (hierarchy.get(i).indexOf(occurrenceOfScope) != -1 && passou < timesThatSpentByTheScope) {//juntas escopos
                        scopesToExamine.addLast(hierarchy.get(i));
                        passou++;
                    }
                }
                String variables[] = null;
                for (int i = 0; i < scopesToExamine.size(); i++) {
                    variables = scopesToExamine.get(i).split(",");
                    //JOptionPane.showMessageDialog(null, variables);
                }
                for (int i = 0; i < variables.length; i++) {
                    variables[i] = "[" + variables[i] + "]";
                    //JOptionPane.showMessageDialog(null, variables[i]);
                }

                boolean ended = false;
                int cont = -1;

                while (!ended) {
                    cont++;
                    for (int i = 0; i <= posFound; i++) {
                        if (codeForEachScope.get(i).indexOf(variables[cont]) != -1) {
                            locatesOccurrencesSql(codeForEachScope.get(i), variablesOfSql, listOfVariables, namesOfMehods);
                        }
                        if (1 + cont >= variables.length) {
                            ended = true;
                        }
                    }
                }
            }
        }
    }

    private void locatesOccurrencesSql(String scopeOfAnalysis, LinkedList<String> variablesOfSQL, LinkedList<String> listOfVariables, LinkedList<String> namesOfMehods) {
        int occurrenceFound = 0;

        String scope = "";
        String identificationOfTheScope = "";
        int openR = -1;
        int closeR = -1;
        //int openParenthesis=-1;
        //int closeParenthesis=-1;

        boolean validtoken = false;
        variablesOfSQL = filterControlsOnList(variablesOfSQL);
        identificationOfTheScope = scopeOfAnalysis.substring(scopeOfAnalysis.indexOf("["), scopeOfAnalysis.indexOf("]") + 1);
        //System.out.println(identificacaoDoEscopo+"ESCOPO");
        for (int i = 0; i < variablesOfSQL.size(); i++) {
            occurrenceFound = 0;
            scope = scopeOfAnalysis;
            //JOptionPane.showMessageDialog(null,"#"+variaveisMetodosDaSql.get(i)+"#");
            while (occurrenceFound != -1) {
                validtoken = false;
                //System.out.println(scope);
                //////verifica se há ocorrência no escopo com código SQL,de alguma das viriáveis SQL encontradas, pegando a sua posição caso haja
                occurrenceFound = scope.indexOf(variablesOfSQL.get(i));
                if (occurrenceFound != -1) {
                    validtoken = tokenVerifies(occurrenceFound, scope, variablesOfSQL.get(i).length());
                    //String teste = scope.substring(occurrenceFound - 10, occurrenceFound + 10);
                    if (validtoken) {
                        //////remove os pulos de linha antes da ocorrência encontrada
                        scope = contrBeforeValid(scope, "\n", occurrenceFound);
                        openR = scope.indexOf("\n");
                        //////remove os ; antes da ocorrência encontrada
                        scope = contrAfterValid(scope, ";", occurrenceFound);
                        closeR = scope.indexOf(";");
                        //scope = contrBeforeValid(scope,"(",occurrenceFound);
                        //openParenthesis = scope.indexOf("(");
                        //scope = contrAfterValid(scope,")",occurrenceFound);
                        //closeParenthesis = scope.indexOf(")");


                        // if(abreR<openParenthesis || openParenthesis == -1){
                        //////coloca no sentToTheModule somente a linha em que se encontra a variável SQL
                        //////também remove o primeiro ; e \n utilizado para pegar a linha
                        if (openR != -1 && openR < occurrenceFound && closeR != -1 && occurrenceFound < closeR) {
                            //escopo =escopo.replaceFirst("\\n","");
                            //int valida = escopo.indexOf("\n");
                            //if(valida>fechaR){
                            sendToTheModule.addLast(identificationOfTheScope + scope.substring(openR + 1, closeR + 1));
                            //JOptionPane.showMessageDialog(null, sendToTheModule.getLast());
                            scope = scope.replaceFirst(";", " ");
                            scope = scope.replaceFirst("\\n", "");
                            //}


                        }
                        /*}else{
                        if(openParenthesis!=-1 && openParenthesis < encontrou && closeParenthesis!=-1 && encontrou<closeParenthesis){
                        sendToTheModule.addLast(identificacaoDoEscopo+escopo.substring(openParenthesis,closeParenthesis+1));
                        escopo =escopo.replaceFirst("\\("," ");
                        escopo =escopo.replaceFirst("\\)"," ");
                        }
                        }*/
                    }
                    if (sendToTheModule.size() > 0) {
                        String aux = sendToTheModule.getLast();
                        //////não entendi
                        aux = aux.replace("!@#", variablesOfSQL.get(i));
                        sendToTheModule.set(sendToTheModule.size() - 1, aux);
                        examinesSelectedLines(sendToTheModule.getLast(), listOfVariables, identificationOfTheScope, namesOfMehods, variablesOfSQL.get(i));
                        //armazena variaveis das linhas encontradas.
                    }
                    String variable = variablesOfSQL.get(i);
                    variable = validTokenToReplaceFirst(variable);
                    scope = scope.replaceFirst(variable, "!@#");
                    //JOptionPane.showMessageDialog(null, scope);
                    //Faz buscar a proxima variavel
                }
            }

        }
        if (scopeOfAnalysis.indexOf("ESCOPOMOVIDO") != -1) {//IDENTIFICACAO DOS METODOS FUNCIONA SEM
            //LinkedList<String> nomesDosMetodos = getNamesOfMethods();
            String lineAnalysis = scopeOfAnalysis.substring(0, scopeOfAnalysis.indexOf("ESCOPOMOVIDO"));
            //System.out.println(lineAnalysis);
            String variaveis = lineAnalysis.substring(lineAnalysis.indexOf("("), lineAnalysis.indexOf(")") + 1);
            boolean found = false;
            for (int i = 0; i < variablesOfSql.size(); i++) {
                if (variaveis.indexOf(variablesOfSQL.get(i)) != -1) {
                    if (tokenVerifies(variaveis.indexOf(variablesOfSQL.get(i)), variaveis, variablesOfSQL.get(i).length())) {

                        //System.out.println(variablesAndMethodsOfSQL.get(i));
                        found = true;
                    }

                }

            }
            //String identificadoresDoMetodo = identificadoresDoMetodo(variaveis);
/*
            if(found){
            for(int i=0; i<nomesDosMetodos.size();i++){
            if(lineAnalysis.indexOf(nomesDosMetodos.get(i))!= -1){
            System.out.println(nomesDosMetodos.get(i)+"ENTROAAAAAAAAAAAAAAAAAAAAAAAA");
            //                     metodosParaAnalisar.addLast(metodo);
            }
            }
            }
             */

        }


    }

    /**
     * 
     * @param scope
     * @param signal
     * @param found
     * @return
     */
    public String contrAfterValid(String scope, String signal, int found) {
        boolean located = false;
        int posSignal = -1;
        String escapeSignal = signal;

        if (signal.equals("\n") || signal.equals("\r") || signal.equals("(") || signal.equals(")")) {
            escapeSignal = "\\" + signal;
        }
        while (!located) {
            posSignal = scope.indexOf(signal);
            if (posSignal > found || posSignal == -1) {
                located = true;
            } else {
                scope = scope.replaceFirst(escapeSignal, " ");
            }
        }
        return scope;
    }

    /**
     * 
     * @param scope
     * @param signal
     * @param found
     * @return
     */
    public String contrBeforeValid(String scope, String signal, int found) {
        boolean located = false;
        int posSignal = -1;
        int posNext = -1;
        String escapeSignal = signal;

        if (signal.equals("\n") || signal.equals("\r") || signal.equals("(") || signal.equals(")")) {
            escapeSignal = "\\" + signal;
        }
        while (!located) {
            posSignal = scope.indexOf(signal);

            if (posSignal < found && posSignal != -1) {
                scope = scope.replaceFirst(escapeSignal, "%");
                posNext = scope.indexOf(signal);
                if (posNext > found || posNext == -1) {
                    located = true;

                    scope = scope.replaceFirst("%", escapeSignal);
                } else {
                    scope = scope.replace("%", " ");
                }
            } else {
                located = true;
            }

        }
        return scope;
    }

    /**
     * 
     * @param list
     * @return Return the list items without space, \r, \n, \r, \b, \f
     */
    public LinkedList<String> filterControlsOnList(LinkedList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace(" ", ""));
            list.set(i, list.get(i).replace("\r", ""));
            list.set(i, list.get(i).replace("\n", ""));
            list.set(i, list.get(i).replace("\t", ""));
            list.set(i, list.get(i).replace("\b", ""));
            list.set(i, list.get(i).replace("\f", ""));

        }
        return list;
    }

    /**
     * 
     * @param code
     * @return the code without space, \r, \n, \t, \b, \f
     */
    public String filterControls(String code) {

        code = code.replace(" ", "");
        code = code.replace("\r", "");
        code = code.replace("\n", "");
        code = code.replace("\t", "");
        code = code.replace("\b", "");
        code = code.replace("\f", "");

        return code;
    }

    private boolean tokenVerifies(int aux, String data, int size) {
        boolean valid = false;
        String letterBefore;
        String letterAfter;

        if (aux - 1 >= 0) {
            letterBefore = data.valueOf(data.charAt(aux - 1));
            valid = isToken(letterBefore);
        } else if (aux == 0) {
            valid = true;
        }
        aux += size;
        if (aux < data.length()) {
            letterAfter = data.valueOf(data.charAt(aux));
            if (valid) {
                valid = isToken(letterAfter);
            }
        }
        return valid;

    }

    private boolean isToken(String letraver) { // verifica se tem letra antes e depois
        String letters = "\\ \\\n\\\t\\\f\\\r\\(\\)\\{\\}\\[\\]\\+\\;\\=\\.\\,";
        boolean valid = false;
        String test = "\\";
        test = test + letraver;
        if (letters.indexOf(test) != -1) {
            valid = true;
        }
        return valid;
    }

    //////pega as variáveis que colocam algum valor nas variáveis SQL
    private void examinesSelectedLines(String code, LinkedList<String> listOfVariables, String identificationOfScope, LinkedList<String> namesOfMehods, String variableExamined) {
        int limiter = -1;
        int posVariable = code.indexOf(variableExamined);
        String variable = "";
        String validatingVariables = "";
        limiter = code.indexOf("=");
        //int variableOfMethod = code.indexOf("(");
        boolean alreadyHave = false;
        //for(int i=0;i<listaVariaveis.size();i++){
        //    System.out.println(listaVariaveis.get(i)+"Variaveis da analise da linha");
        //}



        if (limiter != -1) {
            if (limiter < posVariable && posVariable != -1) {
                variable = code.substring(code.indexOf("##") + 2, limiter);

            } else {
                code = code.substring(limiter);
                limiter = code.indexOf(";");
                if (limiter != -1) {
                    variable = code.substring(1, limiter);
                }
            }
            variable = filterControls(variable);
            alreadyHave = false;
            //////verifica se já tem na lista
            for (int i = 0; i < variablesOfSql.size(); i++) {
                if (variablesOfSql.get(i).indexOf(variable) != -1) {
                    alreadyHave = true;
                }
            }
            if (alreadyHave == false) {
                for (int i = 0; i < listOfVariables.size(); i++) {
                    validatingVariables = listOfVariables.get(i).substring(listOfVariables.get(i).indexOf("|") + 1, listOfVariables.get(i).indexOf("@"));
                    if (validatingVariables.equals(variable)) {
                        variablesOfSql.addLast(variable);
                    }
                }

            }
        }
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getMethodsOfSql() {
        return methodsOfSql;
    }

    /**
     * 
     * @param methodsOfSql
     */
    public void setMethodsOfSql(LinkedList<String> methodsOfSql) {
        this.methodsOfSql = methodsOfSql;
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getSendToTheModule() {
        return sendToTheModule;
    }

    /**
     * 
     * @param sendToTheModule
     */
    public void setSendToTheModule(LinkedList<String> sendToTheModule) {
        this.sendToTheModule = sendToTheModule;
    }

    /**
     * 
     * @param list
     * @return return the list without the repeated items.
     */
    public LinkedList removeRepeatedOccurrences(LinkedList<String> list) {
        String analyzes = "";
        boolean found = false;
        LinkedList<String> newList = new LinkedList();

        for (int i = 0; i < list.size(); i++) {
            analyzes = list.get(i);
            found = false;
            for (int j = 0; j < newList.size(); j++) {
                //System.out.println(newList.get(j));
                if (analyzes.indexOf(newList.get(j)) != -1) {
                    found = true;
                }
            }
            if (!found) {
                newList.addLast(analyzes);
            }
        }

        return newList;
    }

    private String validTokenToReplaceFirst(String variable) {
        variable = variable.replace("(", "\\(");
        variable = variable.replace(")", "\\)");
        variable = variable.replace("{", "\\{");
        variable = variable.replace("}", "\\}");
        variable = variable.replace("[", "\\[");
        variable = variable.replace("]", "\\]");

        return variable;
    }

    /**
     * Adds at nameOfMethods all the lines of code from sendToTheModule list that contains the methods from nameOfMethods
     * @param namesOfMethods It structure is [name of method,code line, name of method, code line,...]
     */
    public void identifiesMethodInLine(LinkedList<String> namesOfMethods) {
        int validToken = -1;
        LinkedList<String> lista = new LinkedList();
        LinkedList<String> sendToTheModuleList = new LinkedList();

        lista = namesOfMethods;

        for (int i = 0; i < sendToTheModule.size(); i++) {
            sendToTheModuleList.addLast(sendToTheModule.get(i));
        }

        for (int i = 0; i < sendToTheModuleList.size(); i++) {
            for (int j = 0; j < linesToAnalyze.size(); j++) {
                if (sendToTheModuleList.get(i).indexOf(linesToAnalyze.get(j)) != -1) {
                    sendToTheModuleList.remove(i);
                }
            }
        }

        for (int i = 0; i < sendToTheModuleList.size(); i++) {
            for (int j = 0; j < lista.size(); j++) {
                if (sendToTheModuleList.get(i).indexOf(lista.get(j)) != -1) {
                    validToken = sendToTheModuleList.get(i).indexOf("(");
                    if (validToken != -1) {
                        validToken = sendToTheModuleList.get(i).indexOf(")");
                        if (validToken != -1) {
                            methodsOfSql.addLast(lista.get(j));
                            //JOptionPane.showMessageDialog(null, methodsOfSql.getLast());
                            //Armazeno o nome do metodo encontrado na linha
                            methodsOfSql.addLast(sendToTheModuleList.get(i));
                            //Armazeno a linha do codigo que contem o metodo
                            //JOptionPane.showMessageDialog(null, methodsOfSql.getLast());
                        }
                    }
                }
            }
        }

    }

    private boolean validTokenOfSQL(String identify) {
        boolean validToken = true;

        if (identify.indexOf("(") != -1 && identify.indexOf(")") != -1) {
            validToken = false;
        }
        return validToken;
    }

    /**
     * 
     * @param namesOfMethods
     * @param codeForEachScope
     * @param listOfMethodsModule
     * @param preparationMethodForAnalyzing
     */
    public void codeOfMethodsToAnalysis(LinkedList<String> namesOfMethods, LinkedList<String> codeForEachScope, LinkedList<String> listOfMethodsModule, LinkedList<String> preparationMethodForAnalyzing) {//busca linhas do metodo todo
        String lineOfMethod = "";
        int posLine = -1;
        String numberOfLine = "";
        boolean validMethod = false;
        LinkedList<String> listOfMethods = new LinkedList();

        //////nao esta sendo utilizado
        for (int i = 0; i < listOfMethodsModule.size(); i++) {
            listOfMethods.addLast(listOfMethodsModule.get(i));
            //JOptionPane.showMessageDialog(null,listOfMethods.get(i) );
        }

        /////////////////continuar daqui
        for (int i = 0; i < methodsOfSql.size(); i++) {
            validMethod = false;
            for (int j = 0; j < codeForEachScope.size(); j++) {
                if (codeForEachScope.get(j).indexOf("ESCOPOMOVIDOMETHOD") != -1) {
                    lineOfMethod = codeForEachScope.get(j).substring(0, codeForEachScope.get(j).indexOf("ESCOPOMOVIDOMETHOD"));

                    if (lineOfMethod.indexOf(methodsOfSql.get(i)) != -1) {
                        //JOptionPane.showMessageDialog(null,"line of method DENTRO"+lineOfMethod );
                        //JOptionPane.showMessageDialog(null,"line of method FORA"+codeForEachScope.get(j)+" ENCONTRA "+methodsOfSql.get(i) );
                        //if(validMethod(methodsOfSql.get(i),listOfMethods,preparationMethodForAnalyzing)){
                        if (validMethod(methodsOfSql.get(i), lineOfMethod, preparationMethodForAnalyzing)) {
                            String scope = lineOfMethod.substring(0, lineOfMethod.indexOf("]") + 1);
                            String aux = "";
                            for (int k = 0; k < codeForEachScope.size(); k++) {
                                if (codeForEachScope.get(k).indexOf(scope) != -1) {
                                    aux = aux.concat(codeForEachScope.get(k));
                                }
                            }

                            posLine = aux.indexOf("## ");
                            aux = contrBeforeValid(aux, "\n", posLine);
                            numberOfLine = aux.substring(aux.indexOf("\n"), posLine);
                            numberOfLine = filterControls(numberOfLine);
                            storesLineNumbers.addLast(numberOfLine);

                            aux = contrBeforeValid(aux, "\n", aux.length());
                            aux = contrBeforeValid(aux, "## ", aux.length());
                            posLine = aux.indexOf("## ");
                            numberOfLine = aux.substring(aux.indexOf("\n"), posLine);
                            numberOfLine = filterControls(numberOfLine);
                            int valid = Integer.parseInt(numberOfLine);
                            storesLineNumbers.addLast(String.valueOf(valid));
                        }
                    }
                }


            }
        }
    }

    /**
     * 
     * @param originalCode
     */
    public void insertMethodsToSendModule(String originalCode) {
        String numLines[] = originalCode.split("\n");
        for (int i = 0; i < numLines.length; i++) {
            numLines[i] = numLines[i].concat("\n");
        }
        String method = "";

        for (int i = 0; i < storesLineNumbers.size(); i = i + 2) {
            int first = Integer.parseInt(storesLineNumbers.get(i));
            int last = Integer.parseInt(storesLineNumbers.get(i + 1));
            method = "";
            for (int j = first - 1; j <= last; j++) {
                method = method.concat(numLines[j]);
            }
            //JOptionPane.showMessageDialog(null, method);
            sendToTheModuleMethod.addLast(method);

        }

    }

    private boolean validMethod(String method, String listOfMethods, LinkedList<String> preparationMethodForAnalyzing) {
        boolean valid = false;
        boolean validField = true;
        //VALIDA SOBRECARGA
        for (int i = 0; i < preparationMethodForAnalyzing.size(); i++) {
            if (preparationMethodForAnalyzing.get(i).indexOf(method) != -1) {
                String typesVariables[] = preparationMethodForAnalyzing.get(i).substring(preparationMethodForAnalyzing.get(i).indexOf("|") + 1, preparationMethodForAnalyzing.get(i).length()).split(",");
                // JOptionPane.showMessageDialog(null, preparationMethodForAnalyzing.get(i)+" metodo "+ method);
                validField = true;
                if (listOfMethods.indexOf(method) != -1) {
                    String typesToValid[] = listOfMethods.substring(listOfMethods.indexOf("(") + 1, listOfMethods.indexOf(")")).split(",");
                    if (typesVariables.length == typesToValid.length) {
                        for (int k = 0; k < typesVariables.length; k++) {
                            if (k < typesToValid.length) {
                                if (typesToValid[k].indexOf(typesVariables[k]) == -1) {
                                    validField = false;
                                    //JOptionPane.showMessageDialog(null, typesToValid[k]+" indexof "+ typesVariables[k]);
                                }
                            } else {
                                validField = false;
                            }
                        }
                    } else {
                        /*  for (int k = 0; k < typesToValid.length; k++) {
                        if (k <= typesVariables.length) {
                        if (typesToValid[k].indexOf(typesVariables[k]) == -1){
                        validField = false;
                        }
                        } else {
                        validField = false;
                        }
                        }*/ validField = false;
                    }
                    if (validField) {
                        valid = true;

                    }
                }

            }
        }
        return valid;
    }

    /**
     * Not utilized. Asks the user if the method have some kind of protection.
     * @return
     */
    public LinkedList<String> questionAboutTheMethods() {
        LinkedList<String> aux_methods = new LinkedList();
        for (int i = 0; i < methodsOfSql.size(); i++) {
            int beingTreated = JOptionPane.showConfirmDialog(null, "O Metodo: " + methodsOfSql.get(i) + " Realiza Validaçao de Entrada e Saida dos Dados?");
            if (beingTreated != 0) {
                aux_methods.addLast(methodsOfSql.get(i));
            }
        }
        return aux_methods;
    }

    /**
     * 
     */
    public void scopeAndLineOfMethodSQL() {
        String identificador = "";
        for (int i = 0; i < sendToTheModule.size(); i++) {
            for (int j = 0; j < linesToAnalyze.size(); j++) {
                identificador = linesToAnalyze.get(j);
                if (sendToTheModule.get(i).indexOf(identificador) != -1) {
                    for (int k = 0; k < methodsOfSql.size(); k++) {
                        if (methodsOfSql.get(k).indexOf("(") != -1 && identificador.indexOf(methodsOfSql.get(k)) != -1) {
                            //JOptionPane.showMessageDialog(null, identificador+"AQUI"+methodsOfSql.get(k));
                            String scopeAndLine = sendToTheModule.get(i).substring(0, sendToTheModule.get(i).indexOf("##") + 3);
                            methodsOfSql.set(k, scopeAndLine.concat(methodsOfSql.get(k)));
                            //JOptionPane.showMessageDialog(null,scopeAndLine+ "Final"+methodsOfSql.get(k));
                            //Insiro nos metodos encontrados o escopo e a linha
                        }

                    }
                }
            }
        }


    }

    private String[] foundVariablesOnMethod(String method) {
        String code = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
        String variables[] = code.split(",");
        for (int i = 0; i < variables.length; i++) {
            variables[i] = filterControls(variables[i]);
        }
        return variables;
    }
    /*private boolean validMethod(String method, LinkedList<String> listOfMethods, LinkedList<String> preparationMethodForAnalyzing) {
    boolean valid=false;
    boolean validField=true;

    for(int i=0;i<preparationMethodForAnalyzing.size();i++){
    if(preparationMethodForAnalyzing.get(i).indexOf(method)!=-1){
    String typesVariables[] = preparationMethodForAnalyzing.get(i).substring(preparationMethodForAnalyzing.get(i).indexOf("|")+1,preparationMethodForAnalyzing.get(i).length()).split(",");
    for(int j=0;j<listOfMethods.size();j++){
    validField=true;
    if(listOfMethods.get(j).indexOf(method)!=-1){
    String typesToValid[] = listOfMethods.get(j).substring(listOfMethods.get(j).indexOf("(")+1,listOfMethods.get(j).indexOf(")")).split(",");
    if(typesVariables.length>=typesToValid.length){
    for(int k=0;k<typesVariables.length;k++){
    if(k<=typesToValid.length){
    if(typesToValid[k].indexOf(typesVariables[k])==-1){
    validField=false;
    }
    }else{validField=false;}
    }
    }else{
    for(int k=0;k<typesToValid.length;k++){
    if(k<=typesVariables.length){
    if(typesToValid[k].indexOf(typesVariables[k])==-1){
    validField=false;
    }
    }else{validField=false;}
    }
    }
    if(validField){
    valid=true;
    listOfMethods.remove(j);
    }
    }
    }
    }
    }
    return valid;
    }*/
}
/*
 *

{
int a=9;
int b=8;
for(int a=8;a<10;a++){}
String sql="SELECT aaaaaaa '"+a+"'bbbbbbbb"+b;
}

 *

 */
