package SRV;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 * 
 * @author Helder
 */
public class identification {

    private LinkedList<String> numOfScope = new LinkedList();
    private LinkedList<String> hashTableConvert = new LinkedList();
    private LinkedList<String> hashTableMarkers = new LinkedList();
    private LinkedList<String> hashTableIdentifiers = new LinkedList();
    private LinkedList<String> listHierarchyOfScopes = new LinkedList();
    private LinkedList<String> codeForEachScope = new LinkedList();
    private LinkedList<String> listOfVariables = new LinkedList();
    private LinkedList<String> listOfMethods = new LinkedList();
    private LinkedList<String> namesOfMethods = new LinkedList();
    private LinkedList<String> preparationMethodForAnalyzing = new LinkedList();

    /**
     * 
     */
    public identification() {
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getPreparationMethodForAnalyzing() {
        return preparationMethodForAnalyzing;
    }

    /**
     * 
     * @param preparationMethodForAnalyzing
     */
    public void setPreparationMethodForAnalyzing(LinkedList<String> preparationMethodForAnalyzing) {
        this.preparationMethodForAnalyzing = preparationMethodForAnalyzing;
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getListHierarchyOfScopes() {
        return listHierarchyOfScopes;
    }

    /**
     * 
     * @param listEcopo
     */
    public void setListHierarchyOfScopes(LinkedList<String> listEcopo) {
        this.listHierarchyOfScopes = listEcopo;
    }


    //////Faz a identificação de escopos no código
    /*Exemplo
     *   {
     *          while(a<10){
     *          }
     *   }
     *
     * vira isso:
     *
     *   0## H1E1T1
     *   1## 	while(a<10)H1E2T2
     *   2## 	 H1F2T2
     *   3##
     *   4##  H1F1T1
     *
     */
    /**
     * 
     * @param code
     * @return
     */
    public String makeIdentification(String code) {
        int hierarchyNum = 0;
        int scopeNum = 0;
        int hierarchyContr = 0;
        int scopeAll = 0;
        boolean openEnded = false;
        boolean closeEnded = false;
        String scopeClose = "";
        LinkedList<String> scopeStack = new LinkedList();
        LinkedList<String> scopeCloseContr = new LinkedList();

        while (!openEnded || !closeEnded) {
            int open = code.indexOf("{");
            int close = code.indexOf("}");
            if (close == -1 && open == -1) {
                closeEnded = true;
                openEnded = true;
            }
            if (close != -1) {
                if (open != -1) {
                    if (open < close) {
                        if (scopeStack.size() == 0) {
                            hierarchyNum++;
                            hierarchyContr++;
                            scopeNum = 0;
                        }
                        scopeNum++;
                        scopeAll++;
                        scopeStack.addFirst("{");
                        code = code.replaceFirst("\\{", "H" + hierarchyNum + "E" + scopeNum + "T" + scopeAll + " ");
                        setNumOfScope("H" + hierarchyNum + "E" + scopeNum + "T" + scopeAll);
                        scopeCloseContr.addFirst("H" + hierarchyNum + "E" + scopeNum + "T" + scopeAll);
                        String escopos = "[" + getLastScope() + "]";
                        for (int i = 0; i < scopeCloseContr.size(); i++) {
                            escopos = escopos.concat("," + scopeCloseContr.get(i));
                        }
                        listHierarchyOfScopes.addLast(escopos);
                    } else {
                        scopeStack.addFirst("}");
                        if (scopeStack.get(1).equals("{") && scopeStack.getFirst().equals("}")) {
                            scopeClose = (String) scopeCloseContr.getFirst();
                            scopeClose = scopeClose.substring(scopeClose.indexOf("E") + 1, scopeClose.length());
                            code = code.replaceFirst("\\}", " " + "H" + hierarchyContr + "F" + scopeClose + " ");
                            setNumOfScope("H" + hierarchyNum + "F" + scopeClose);
                            scopeCloseContr.removeFirst();
                            scopeStack.removeFirst();
                            scopeStack.removeFirst();
                        }
                    }
                } else {
                    openEnded = true;
                    scopeStack.addFirst("}");
                    if (scopeStack.get(1).equals("{") && scopeStack.getFirst().equals("}")) {
                        scopeClose = (String) scopeCloseContr.getFirst();
                        scopeClose = scopeClose.substring(scopeClose.indexOf("E") + 1, scopeClose.length());
                        code = code.replaceFirst("\\}", " " + "H" + hierarchyContr + "F" + scopeClose);
                        setNumOfScope("H" + hierarchyContr + "F" + scopeClose);
                        scopeCloseContr.removeFirst();
                        scopeStack.removeFirst();
                        scopeStack.removeFirst();
                    }
                }
            } else {
                closeEnded = true;
            }

            for(int i =0 ;i <listHierarchyOfScopes.size();i++){
                System.out.println("LIST HIERARQUIA"+listHierarchyOfScopes.get(i));
            }
            for(int i =0 ;i <numOfScope.size();i++){
                System.out.println("NUM ESCOPO"+numOfScope.get(i));
            }
        }
        return code;
    }

    /**
     * 
     * @param code
     * @return
     */
    public String ignoreComments(String code) {
        boolean end = false;
        boolean carriageReturn = true;
        String beginningOfCode;
        String finalOfCode;
        int found = 0;
        int foundSingleQuotes = -1;
        int foundDoubleQuotes = -1;
        int foundBarBar = -1;
        int foundBarStar = -1;
        int nextOccurrence = -1;
        LinkedList<Integer> smaller = new LinkedList();

        while (!end) {
            nextOccurrence = -1;
            smaller.clear();
            foundSingleQuotes = code.indexOf("'");
            foundDoubleQuotes = code.indexOf("\"");
            foundBarBar = code.indexOf("//");
            foundBarStar = code.indexOf("/*");
            smaller.addFirst(foundSingleQuotes);
            smaller.addFirst(foundDoubleQuotes);
            smaller.addFirst(foundBarBar);
            smaller.addFirst(foundBarStar);
            smaller = findFirstOccurrence(smaller);
            if (smaller.size() > 0) {
                if (smaller.get(0) == foundSingleQuotes) {
                    nextOccurrence = 1;
                } else if (smaller.get(0) == foundDoubleQuotes) {
                    nextOccurrence = 2;
                } else if (smaller.get(0) == foundBarBar) {
                    nextOccurrence = 3;
                } else if (smaller.get(0) == foundBarStar) {
                    nextOccurrence = 4;
                } else {
                    nextOccurrence = -1;
                }
            }
            switch (nextOccurrence) {
                case 1: {
                    String ignore = "";
                    int pos1 = -1;
                    int pos2 = -1;
                    found = code.indexOf("'");
                    pos1 = found;
                    code = code.replaceFirst("'", "~~"); //////começo
                    found = code.indexOf("'");
                    pos2 = found;

                    code = code.replaceFirst("'", "!!!"); //////fim
                    if (found != -1) {
                        ignore = code.substring(pos1, pos2 + 3);
                        int validatesComment = -1;
                        for (int i = 0; i < hashTableMarkers.size(); i++) {
                            //////verifica quais palavras da lista hashTableMarkers existe no código
                            validatesComment = ignore.indexOf(hashTableMarkers.get(i));
                            if (validatesComment != -1) {
                                if (hashTableMarkers.get(i).equals("{") || hashTableMarkers.get(i).equals("}")
                                        || hashTableMarkers.get(i).equals("(") || hashTableMarkers.get(i).equals(")")
                                        || hashTableMarkers.get(i).equals("\"") || hashTableMarkers.get(i).equals("*/")
                                        || hashTableMarkers.get(i).equals("/*")) {
                                    while (ignore.indexOf(hashTableMarkers.get(i)) != -1) {
                                        if (hashTableMarkers.get(i).equals("*/") || hashTableMarkers.get(i).equals("/*")) {
                                            if (hashTableMarkers.get(i).equals("/*")) {
                                                ignore = ignore.replaceFirst("/\\*", hashTableConvert.get(i));
                                            } else {
                                                ignore = ignore.replaceFirst("\\*/", hashTableConvert.get(i));
                                            }
                                        } else {
                                            ignore = ignore.replaceFirst("\\" + hashTableMarkers.get(i), hashTableConvert.get(i));
                                        }
                                    }
                                } else {
                                    while (ignore.indexOf(hashTableMarkers.get(i)) != -1) {
                                        ignore = ignore.replaceFirst(hashTableMarkers.get(i), hashTableConvert.get(i));
                                    }
                                }
                            }
                        }
                        beginningOfCode = code.substring(0, code.indexOf("~~"));
                        finalOfCode = code.substring(code.indexOf("!!!") + 3, code.length());
                        code = beginningOfCode.concat(ignore.concat(finalOfCode));
                        code = code.replaceFirst("~~", hashTableConvert.get(11));
                        code = code.replaceFirst("!!!", hashTableConvert.get(11));
                        
                    }
                }
                break;
                case 2: {
                    String ignore = "";
                    int pos1 = -1;
                    int pos2 = -1;
                    found = code.indexOf("\"");
                    pos1 = found;
                    code = code.replaceFirst("\"", "%%");
                    found = code.indexOf("\"");
                    pos2 = found;
                    if (pos2 != -1) {
                        if (code.charAt(pos2 - 1) == '\\') {
                            code = code.replaceFirst("\"", "scape");
                            found = code.indexOf("\"");
                            pos2 = found;
                        }
                    }
                    code = code.replaceFirst("\"", "&&&");
                    if (found != -1) {
                        ignore = code.substring(pos1, pos2 + 3);
                        int validatesComment = -1;
                        for (int i = 0; i < hashTableMarkers.size(); i++) {
                            validatesComment = ignore.indexOf(hashTableMarkers.get(i));
                            if (validatesComment != -1) {
                                if (hashTableMarkers.get(i).equals("{") || hashTableMarkers.get(i).equals("}")
                                        || hashTableMarkers.get(i).equals("(") || hashTableMarkers.get(i).equals(")")
                                        || hashTableMarkers.get(i).equals("\"") || hashTableMarkers.get(i).equals("*/")
                                        || hashTableMarkers.get(i).equals("/*")) {
                                    while (ignore.indexOf(hashTableMarkers.get(i)) != -1) {
                                        if (hashTableMarkers.get(i).equals("*/") || hashTableMarkers.get(i).equals("/*")) {
                                            if (hashTableMarkers.get(i).equals("/*")) {
                                                ignore = ignore.replaceFirst("/\\*", hashTableConvert.get(i));
                                            } else {
                                                ignore = ignore.replaceFirst("\\*/", hashTableConvert.get(i));
                                            }
                                        } else {
                                            ignore = ignore.replaceFirst("\\" + hashTableMarkers.get(i), hashTableConvert.get(i));
                                        }
                                    }
                                } else {
                                    while (ignore.indexOf(hashTableMarkers.get(i)) != -1) {
                                        ignore = ignore.replaceFirst(hashTableMarkers.get(i), hashTableConvert.get(i));
                                    }
                                }
                            }
                        }
                        beginningOfCode = code.substring(0, code.indexOf("%%"));
                        finalOfCode = code.substring(code.indexOf("&&&") + 3, code.length());
                        code = beginningOfCode.concat(ignore.concat(finalOfCode));
                        //System.out.println(code);
                        code = code.replaceFirst("%%", hashTableConvert.get(10));
                        //System.out.println(code);
                        code = code.replaceFirst("&&&", hashTableConvert.get(10));
                        //System.out.println(code);
                    }
                }
                break;
                case 3: {
                    found = code.indexOf("//");
                    code = code.replaceFirst("\\//", "+_");
                    carriageReturn = true;
                    if (found != -1) {
                        while (carriageReturn) {
                            found = code.indexOf("\r");
                            if (found < code.indexOf("+_")) {
                                code = code.replaceFirst("\\r", "&FAIL&");
                            } else {
                                carriageReturn = false;
                                code = code.replaceFirst("\\r", "***");
                            }
                        }
                        carriageReturn = true;
                        while (carriageReturn) {
                            found = code.indexOf("&FAIL&");
                            if (found != -1) {
                                code = code.replaceFirst("&FAIL&", "\\\r");
                            } else {
                                carriageReturn = false;
                            }
                        }
                        if (code.indexOf("+_") != -1) {
                            beginningOfCode = code.substring(0, code.indexOf("+_"));
                            finalOfCode = code.substring(code.indexOf("***") + 3, code.length());
                            code = beginningOfCode.concat(finalOfCode);
                        }
                    }
                }
                break;
                case 4: {
                    found = code.indexOf("/*");
                    code = code.replaceFirst("/\\*", "!@");
                    found = code.indexOf("*/");
                    code = code.replaceFirst("\\*/", "#%#");
                    if (found != -1) {
                        beginningOfCode = code.substring(0, code.indexOf("!@"));
                        finalOfCode = code.substring(code.indexOf("#%#") + 3, code.length());
                        code = beginningOfCode.concat(finalOfCode);
                    }
                }
                break;
                case -1: {
                    end = true;
                }
                ;
                break;
            }

        }
        code = code.replace(hashTableConvert.get(10), "\"");
        code = code.replace(hashTableConvert.get(11), "'");
        //System.out.println(code);
        return code;
    }

    /**
     * 
     * @param oldHashTableMarkers
     */
    public void setHashTableMarkers(LinkedList<String> oldHashTableMarkers) {
        this.hashTableMarkers = oldHashTableMarkers;
    }

    /**
     * 
     * @param index
     * @return
     */
    public String getElementOfHashTableMarkers(int index) {
        return this.hashTableMarkers.get(index);
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getHashTableConvert() {
        return hashTableConvert;
    }

    /**
     * 
     * @param hashTable2
     */
    public void setHashTableConvert(LinkedList<String> hashTable2) {
        this.hashTableConvert = hashTable2;
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getHashTableIdentifiers() {
        return hashTableIdentifiers;
    }

    /**
     * 
     * @param tabelaHash
     */
    public void setHashTableIdentifiers(LinkedList<String> tabelaHash) {
        this.hashTableIdentifiers = tabelaHash;
    }

    /**
     * 
     * @param i
     * @return
     */
    public String getElementHashTableIdentifiers(int i) {
        return (String) hashTableIdentifiers.get(i);
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getListHierarchyScopes() {
        return listHierarchyOfScopes;
    }

    /**
     * 
     * @param ListHierarchyScopes
     */
    public void setListHierarchyScopes(LinkedList<String> ListHierarchyScopes) {
        this.listHierarchyOfScopes = ListHierarchyScopes;
    }

    /**
     * 
     * @param code
     * @return
     */
    public String relocateScope(String code) {

        for (int i = 0; i < hashTableIdentifiers.size(); i++) {
            int found = 0;
            int scopeNum = 0;
            while (found != -1) {
                found = code.indexOf(getElementHashTableIdentifiers(i));
                if (found != -1) {
                    boolean scopeFound = false;
                    int j = 0;
                    while (!scopeFound) {

                        scopeNum = code.indexOf((String) getNumOfScope().get(j++));

                        if (found < scopeNum) {
                            scopeFound = true;
                            //////verifica qual hashcode o escopo é igual para definir a mensagem
                            if (hashTableIdentifiers.get(i).equals("%101577%") || hashTableIdentifiers.get(i).equals("%94432955%")) {
                                //////troca a hierarquia do escopo pelo ESCOPOMOVIDO
                                code = code.replaceFirst((String) getNumOfScope().get(j - 1), "ESCOPOMOVIDO");

                                //////troca o hashcode que estava antes da função pela hierarquia do escopo
                                code = code.replaceFirst((String) hashTableIdentifiers.get(i), (String) getNumOfScope().get(j - 1));
                            } else {
                                //////idem ao de cima
                                code = code.replaceFirst((String) getNumOfScope().get(j - 1), "ESCOPOMOVIDOMETHOD");
                                //System.out.println(getNumOfScope().get(j-1)+"  "+(String) hashTableIdentifiers.get(i));
                                code = code.replaceFirst((String) hashTableIdentifiers.get(i), (String) getNumOfScope().get(j - 1));

                            }

                        }
                    }
                }
            }
        }
        return code;
    }

    private boolean isIdentifierMethod(int identifierPos, String text) {
        boolean isMethod = false;
        int parenthesisFound;
        int semicolonFound;
        int equalFound;
        int smaller;
        String textEditor = text;


        textEditor = textEditor.substring(identifierPos);
        parenthesisFound = textEditor.indexOf("(");
        semicolonFound = textEditor.indexOf(";");
        equalFound = textEditor.indexOf("=");

        if (equalFound != -1) {
            if (semicolonFound != -1) {
                if (equalFound < semicolonFound) {
                    smaller = equalFound;
                } else {
                    smaller = semicolonFound;
                }
            } else {
                smaller = equalFound;
            }
        } else {
            smaller = semicolonFound;
        }

        if (parenthesisFound != -1) {
            if (smaller != -1) {
                if (parenthesisFound < smaller) {
                    isMethod = true;
                }
            } else {
                isMethod = true;
            }
        } else {
            isMethod = false;
        }
        return isMethod;
    }

    private void storeVariables(String analysisCode, String identifier, String scope, String typeVariable) {

        analysisCode = analysisCode.replace("\n", "");
        analysisCode = analysisCode.replace("\t", "");
        analysisCode = analysisCode.replace("\f", "");
        analysisCode = analysisCode.replace("\r", "");
        analysisCode = analysisCode.replace(" ", "");


        int equal = analysisCode.indexOf("=");
        if (equal != -1) {
            String variable = analysisCode.substring(0, equal);
            addVariable(identifier + ":|" + variable + "@Escopo: %" + scope + "#Type: " + typeVariable);

        } else {
            String variables[] = analysisCode.split(",");
            int cont = 0;
            int equals = -1;

            while (cont < variables.length) {
                if (variables[cont] == null || variables[cont].equals("")) {
                    cont++;
                } else {
                    equals = variables[cont].indexOf("=");
                    if (equals != -1) {
                        variables[cont] = variables[cont].substring(0, equals);
                    }
                    addVariable(identifier + ":|" + variables[cont++] + "@Escopo: %" + scope + "#Type: " + typeVariable);
                }
            }
        }

    }

    /**
     * 
     * @return
     */
    public LinkedList getListOfVariables() {
        return listOfVariables;
    }

    /**
     * 
     * @param variable
     */
    public void addVariable(String variable) {
        this.listOfVariables.addLast(variable);
    }

 

    /**
     * 
     * @param text
     * @param identifiers
     * @return
     */
    public String markingIdentifiers(String text, LinkedList<String> identifiers) {
        boolean ended = false;
        //Realiza validacao dos identificadores ex public private, criptografando e marcando ocmo fail aqueles que nao sao
        while (!ended) {
            boolean validToken = false;
            boolean isMethod = false;
            for (int i = 0; i < identifiers.size(); i++) {
                int found = 0;
                String marker = "@Fail@";

                while (found != -1) {
                    found = text.indexOf(identifiers.get(i));
                    String identifier = identifiers.get(i);
                    int identMarkHash = identifier.hashCode();
                    validToken = false;

                    if (found != -1) {
                        //System.out.println(identifier+"------"+identMarkHash);
                        validToken = verifiesToken(found, text, identifier.length());
                        if (!validToken) {
                            text = text.replaceFirst(identifier, marker);
                        } else {
                            isMethod = isIdentifierMethod(found, text);
                            if (isMethod) {
                                text = text.replaceFirst(identifier, String.valueOf("%" + identMarkHash + "%"));
                            } else {
                                text = text.replaceFirst(identifier, marker);
                            }
                        }
                    }
                }
            }
            ended = true;
        }
        return text;
    }

    private boolean verifiesToken(int aux, String data, int size) {
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

    private boolean isToken(String letterVer) { // verifica se tem letra antes e depois
        String letters = "\\ \\\n\\\t\\\f\\\r\\(\\)\\{\\}//[//]";
        boolean valid = false;
        String test = "\\";
        test = test + letterVer;
        if (letters.indexOf(test) != -1) {
            valid = true;
        }
        return valid;
    }

    /**
     * 
     * @return
     */
    public LinkedList getNumOfScope() {
        return numOfScope;
    }

    /**
     * 
     * @param scope
     */
    public void setNumOfScope(String scope) {
        this.numOfScope.addLast(scope);
    }

    /**
     * 
     * @param pos
     * @return
     */
    public String getElementOfNumOfScope(int pos) {
        return (String) numOfScope.get(pos);
    }

    private String getLastScope() {
        return this.numOfScope.getLast();
    }

    /**
     * 
     * @param text
     * @param primitiveTypes
     */
    public void variablesFound(String text, LinkedList primitiveTypes) {
        LinkedList<String> stackScope = new LinkedList();
        LinkedList<String> stackControl = new LinkedList();

        int pos = 0;
        String codAnalysis = "";
        stackScope = getNumOfScope();
        stackControl.addFirst(stackScope.getFirst());

        while (pos < stackScope.size() - 1) {
            String initialPosition = (String) stackScope.get(pos);
            String finalPosition = (String) stackScope.get(++pos);

            codAnalysis = text.substring(text.indexOf(initialPosition), text.indexOf(finalPosition));
            if (stackControl.size() > 0) {
                analyzesVariables(codAnalysis, primitiveTypes, stackControl.getFirst());
                codeForEachScope.addLast("[" + stackControl.getFirst() + "]" + codAnalysis);
            }//Cria a lista do código de cada escopo
            if (finalPosition.indexOf("E") != -1) {
                stackControl.addFirst(finalPosition);
            } else {
                stackControl.removeFirst();
            }
        }
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getCodeForEachScope() {
        return codeForEachScope;
    }

    /**
     * 
     * @param CodeForEachScope
     */
    public void setCodeForEachScope(LinkedList<String> CodeForEachScope) {
        this.codeForEachScope = CodeForEachScope;
    }

    private void analyzesVariables(String analyzesCode, LinkedList primitiveTypes, String scope) {
        boolean ended = false;
        String codAux = "";

        while (!ended) {
            boolean validToken = false;
            codAux = analyzesCode;
            String typeVariable = "";
            boolean hasMore = true;
            while (hasMore) {
                typeVariable = "";
                int found = 999999999;
                int pos = 0;
                for (int i = 0; i < primitiveTypes.size(); i++) {
                    int smaller = codAux.indexOf((String) primitiveTypes.get(i));
                    if (smaller != -1 && smaller < found) {
                        found = smaller;
                        pos = i;
                    }
                }
                String identifier = (String) primitiveTypes.get(pos);
                validToken = false;
                //retorna posicao do primeiro tipo primitivo que aparece
                if (found != 999999999) {
                    validToken = verifiesToken(found, codAux, identifier.length());
                    if (!validToken) {
                        codAux = codAux.replaceFirst(identifier, "FAIL");
                    } else {
                        int SearchOpenParenthesis = codAux.indexOf("(");
                        int SearchCloseParenthesis = codAux.indexOf(")");
                        int semicolon = codAux.indexOf(";");
                        boolean verCloseParenthesis = true;
                        boolean verSemicolon = true;
                        while (verCloseParenthesis || verSemicolon) {
                            SearchOpenParenthesis = codAux.indexOf("(");
                            SearchCloseParenthesis = codAux.indexOf(")");
                            semicolon = codAux.indexOf(";");
                            if (SearchCloseParenthesis != -1 && SearchCloseParenthesis < found) {
                                codAux = codAux.replaceFirst("\\)", " ");
                                codAux = codAux.replaceFirst("\\(", " ");
                            } else {
                                verCloseParenthesis = false;
                            }
                            if (semicolon != -1 && semicolon < found) {
                                codAux = codAux.replaceFirst(";", " ");
                            } else {
                                verSemicolon = false;
                            }
                        }
                        // remove parenteses e ; ate chegar na variavel
                        if (SearchOpenParenthesis != -1 && SearchCloseParenthesis != -1) {
                            if (SearchOpenParenthesis < found && found < SearchCloseParenthesis) {//METODO ou for
                                int comma = codAux.indexOf(",");
                                boolean commaIgnore = true;
                                if (comma != -1 && comma < SearchCloseParenthesis && comma > SearchOpenParenthesis) {//metodo
                                    while (commaIgnore) {
                                        if (found > comma) {
                                            codAux = codAux.replaceFirst(",", " ");
                                            comma = codAux.indexOf(",");
                                            if (comma == -1) {
                                                commaIgnore = false;
                                            }
                                        } else {
                                            commaIgnore = false;
                                        }

                                    }
                                    if (comma != -1 && comma > SearchCloseParenthesis) {
                                        typeVariable = "method";
                                        storeVariables(codAux.substring(found + identifier.length(), SearchCloseParenthesis), identifier, scope, typeVariable);
                                        codAux = codAux.replaceFirst(identifier, "ANALISADO");
                                        codAux = codAux.replaceFirst("\\)", " ");
                                        codAux = codAux.replaceFirst("\\(", " ");
                                    } else {
                                        typeVariable = "method";
                                        storeVariables(codAux.substring(found + identifier.length(), codAux.indexOf(",")), identifier, scope, typeVariable);
                                        codAux = codAux.replaceFirst(identifier, "ANALISADO");
                                        codAux = codAux.replaceFirst(",", " ");
                                    }
                                } else if (semicolon != -1 && semicolon < SearchCloseParenthesis && semicolon > SearchOpenParenthesis) {//FOR
                                    typeVariable = "for";
                                    storeVariables(codAux.substring(found + identifier.length(), codAux.indexOf(";")), identifier, scope, typeVariable);
                                    codAux = codAux.replaceFirst(identifier, "ANALISADO");

                                    boolean noOccurrence = false;
                                    while (!noOccurrence) {
                                        semicolon = codAux.indexOf(";");
                                        SearchCloseParenthesis = codAux.indexOf(")");
                                        if (semicolon != -1 && semicolon < SearchCloseParenthesis) {
                                            codAux = codAux.replaceFirst(";", " ");
                                        } else {
                                            noOccurrence = true;
                                        }
                                    }
                                    codAux = codAux.replaceFirst("\\)", " ");
                                    codAux = codAux.replaceFirst("\\(", " ");
                                } else {
                                    typeVariable = "method";
                                    storeVariables(codAux.substring(found + identifier.length(), SearchCloseParenthesis), identifier, scope, typeVariable);
                                    codAux = codAux.replaceFirst(identifier, "ANALISADO");
                                    codAux = codAux.replaceFirst("\\)", " ");
                                    codAux = codAux.replaceFirst("\\(", " ");
                                }

                            } else if (found < semicolon /*&& pontovirgula < buscaAbreParentese*/) {
                                boolean scopeVer = false;
                                int scopeMoved = -1;
                                while (!scopeVer) {
                                    scopeMoved = codAux.indexOf("ESCOPOMOVIDO");
                                    if (scopeMoved > found || scopeMoved == -1) {
                                        scopeVer = true;
                                    } else {
                                        codAux = codAux.replaceFirst("ESCOPOMOVIDO", "            ");
                                    }
                                }
                                if (scopeMoved != -1 && scopeMoved < semicolon) {
                                    codAux = codAux.replaceFirst(identifier, "FAIL");
                                } else {
                                    typeVariable = "default";
                                    storeVariables(codAux.substring(found + identifier.length(), codAux.indexOf(";")), identifier, scope, typeVariable);
                                    codAux = codAux.replaceFirst(identifier, "ANALISADO");
                                    codAux = codAux.replaceFirst(";", " ");
                                }
                            } else {
                                codAux = codAux.replaceFirst(identifier, "FAIL");
                            }
                        } else {
                            int semiColon = codAux.indexOf(";");
                            if (semiColon != -1) {
                                typeVariable = "default";
                                storeVariables(codAux.substring(found + identifier.length(), semiColon), identifier, scope, typeVariable);
                                codAux = codAux.replaceFirst(identifier, "ANALISADO");
                                codAux = codAux.replaceFirst(";", " ");
                            }
                        }
                    }

                } else {
                    hasMore = false;
                }

            }
            ended = true;
        }

    }

    //////organiza de forma crescente
    private LinkedList<Integer> findFirstOccurrence(LinkedList<Integer> smaller) {
        int compare;
        int compareNext;
        int aux;

        for (int j = 0; j < smaller.size(); j++) {
            for (int i = 0; i < smaller.size() - 1; i++) {
                compare = smaller.get(i);
                compareNext = smaller.get(i + 1);
                if (compare > compareNext) {
                    aux = smaller.get(i);
                    smaller.set(i, compareNext);
                    smaller.set(i + 1, aux);
                }
            }
        }

        boolean continues = true;
        while (continues && (smaller.size() > 0)) {
            if ((smaller.get(0) == -1)) {
                smaller.removeFirst();
            } else {
                continues = false;
            }
        }
        return smaller;

    }

    /**
     * 
     */
    public void identifyListOfMethods() {
        int methodFound = -1;
        String lineOfMethod = "";
        for (int i = 0; i < codeForEachScope.size(); i++) {
            methodFound = codeForEachScope.get(i).indexOf("ESCOPOMOVIDOMETHOD");
            if (methodFound != -1) {
                lineOfMethod = codeForEachScope.get(i).substring(0, methodFound);
                lineOfMethod = lineOfMethod.substring(0, lineOfMethod.indexOf(")") + 1);
                //////caso o método seja declarado em várias linhas, ele é separado e jogado no vetor moreLines
                String moreLines[] = lineOfMethod.split("\n");

                if (moreLines.length > 2) {
                    lineOfMethod = "";
                    for (int j = 0; j < moreLines.length; j++) {
                        if (moreLines[j].indexOf("##") != -1) {
                            //////junta as linhas para formar uma única linha, sem colocar o número da linha
                            //////transformando ele em um único comando
                            lineOfMethod = lineOfMethod.concat(" " + moreLines[j].substring(moreLines[j].indexOf("##") + 2));
                            System.out.println(lineOfMethod);
                        } else {
                            lineOfMethod = lineOfMethod.concat(moreLines[j] + " ");
                        }
                    }
                }
                System.out.println(lineOfMethod);

                lineOfMethod = lineOfMethod.replace("\n", "#");
                lineOfMethod = lineOfMethod.replace("\t", "#");
                lineOfMethod = lineOfMethod.replace("\f", "#");
                lineOfMethod = lineOfMethod.replace("\r", "#");
                lineOfMethod = lineOfMethod.replace(" ", "#");
                System.out.println(lineOfMethod);
                boolean valid = false;
                int found = -1;
                while (!valid) {
                    found = lineOfMethod.indexOf("#");
                    if (found != -1) {
                        //////remove multiplos # seguidos
                        while (String.valueOf(lineOfMethod.charAt(found + 1)).equals("#")) {
                            lineOfMethod = lineOfMethod.replaceFirst("#", "");
                            System.out.println(lineOfMethod);
                            found = lineOfMethod.indexOf("#");
                        }
                    } else {
                        valid = true;
                    }
                    //////substitui um único # por espaço
                    lineOfMethod = lineOfMethod.replaceFirst("#", " ");
                    System.out.println(lineOfMethod);
                    //JOptionPane.showMessageDialog(null, lineOfMethod);
                    //Este metodo realiza endentaçao da declaracao dos metodos.
                }
                if (!lineOfMethod.equals("")) {
                    listOfMethods.addLast(lineOfMethod);
                }
            }

        }


    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getListOfMethods() {
        return listOfMethods;
    }

    /**
     * 
     * @param listOfMethods
     */
    public void setListOfMethods(LinkedList<String> listOfMethods) {
        this.listOfMethods = listOfMethods;
    }

    /**
     * 
     * @return
     */
    public LinkedList<String> getNamesOfMethods() {
        return namesOfMethods;
    }

    /**
     * 
     * @param namesOfMethods
     */
    public void setNamesOfMethods(LinkedList<String> namesOfMethods) {
        this.namesOfMethods = namesOfMethods;
    }

    /**
     * 
     */
    public void identifiesMethodsName() {
        LinkedList<String> methods = new LinkedList();
        for (int i = 0; i < listOfMethods.size(); i++) {
            methods.addLast(listOfMethods.get(i));
        }

        String name = "";
        int valid = -1;
        boolean ended;

        for (int i = 0; i < methods.size(); i++) {
            name = methods.get(i).substring(0, methods.get(i).indexOf("("));
            if (String.valueOf(name.charAt(name.length() - 1)).equals(" ")) {
                name = name.substring(0, name.length() - 1);
            }
            ended = false;
            //////vai excluindo do name a parte que não é o nome do método
            while (!ended) {
                valid = name.indexOf(" ");
                if (valid != -1) {
                    name = name.replaceFirst(" ", "#");
                    valid = name.indexOf(" ");
                    if (valid != -1) {
                        name = name.substring(name.indexOf("#") + 1);
                    } else {
                        name = name.substring(name.indexOf("#") + 1);
                        ended = true;
                    }
                }
            }
            methods.set(i, name);
        }
        namesOfMethods = methods;
    }

    /**
     * 
     * @param code
     * @return
     */
    public String numberOfLines(String code) {
        String[] lines = code.split("\n");
        String txt = "";

        for (int i = 0; i < lines.length; i++) {
            txt = txt.concat(i + "## " + lines[i].concat("\n"));
        }

        return txt;
    }

    /**
     * 
     * @param methodOfSql
     */
    public void identifyOnlyVariablesOnMethods(LinkedList<String> methodOfSql) {
        String scope = "";
        int openParenthesis = -1;
        int closeParenthesis = -1;
        String variables[];
        String methodName = "";
        String scopefound = "";

        String conctTypeVaribles = "";
        boolean found = false;
        //ESTE METODO RESOLVE UM METODO POR LINHA
        for (int i = 0; i < methodOfSql.size(); i++) {
            scopefound = scope = methodOfSql.get(i).substring(0, methodOfSql.get(i).indexOf("]") + 1);
            variables = null;
            methodName = "";
            found = false;

            /*for(int j=0; j< methodOfSql.size();j++){
                JOptionPane.showMessageDialog(null, methodOfSql.get(j));
            }*/

            conctTypeVaribles = "";
            openParenthesis = methodOfSql.get(i).indexOf("(");
            closeParenthesis = methodOfSql.get(i).indexOf(")");
            if (openParenthesis != -1 && closeParenthesis != -1) {
                methodName = identifiesMethodInLine(methodOfSql.get(i));
                //JOptionPane.showMessageDialog(null,methodName+"----"+ methodOfSql.get(i));
                variables = methodOfSql.get(i).substring(openParenthesis + 1, closeParenthesis).split(",");
                for (int j = 0; j < variables.length; j++) {
                    variables[j] = filterControls(variables[j]);
                    //System.out.println(variables[j]+"ROYYYYYYYYYYYYYYYYYYYYY");
                }
                for (int k = 0; k < listHierarchyOfScopes.size(); k++) {
                    if (listHierarchyOfScopes.get(k).indexOf(scope) != -1) {
                        scope = listHierarchyOfScopes.get(k);
                    }
                }
                // System.out.println(scope+"ESCOPO DA VARIAVEL");
                String scopes[] = scope.split(",");
                for (int x = 0; x < variables.length; x++) {
                    for (int w = 0; w < listOfVariables.size(); w++) {
                        if (listOfVariables.get(w).indexOf(variables[x]) != -1) {
                            for (int y = 0; y < scopes.length; y++) {
                                if (listOfVariables.get(w).indexOf(scopes[y]) != -1) {
                                    conctTypeVaribles = conctTypeVaribles.concat(listOfVariables.get(w).substring(0, listOfVariables.get(w).indexOf(":")) + ",");
                                    //JOptionPane.showMessageDialog(null,"variables "+variables[x]+" listOfVariables.get(w) "+listOfVariables.get(w)+" scopes[y] "+(scopes[y]));
                                    //JOptionPane.showMessageDialog(null,"sobrecarga "+conctTypeVaribles);
                                    //Identifica as variaveis dos metodos e Armazena os tipos dos parametros que deverao ser validados na sobrecarga
                                    found = true;
                                }
                            }
                        }
                    }
                }
                if (found) {
                    preparationMethodForAnalyzing.addLast(scopefound + "#" + methodName + "|" + conctTypeVaribles);
                    JOptionPane.showMessageDialog(null, preparationMethodForAnalyzing.getLast());
                    //Esta lista contem a estrutura dos metodos para validacao de sobrecarga
                }
            }

        }

    }

    /**
     * 
     * @param line
     * @return
     */
    public String identifiesMethodInLine(String line) {

        int validToken = -1;
        String methodName = "";
        LinkedList<String> lista = new LinkedList();
        for (int i = 0; i < namesOfMethods.size(); i++) {
            lista.addLast(namesOfMethods.get(i));
        }
        for (int j = 0; j < lista.size(); j++) {
            if (line.indexOf(lista.get(j)) != -1) {
                validToken = line.indexOf("(");
                if (validToken != -1) {
                    validToken = line.indexOf(")");
                    if (validToken != -1) {
                        methodName = lista.get(j);
                    }
                }
            }
        }
        return methodName;
    }

    /**
     * 
     * @param code
     * @return
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

    /**
     * 
     * @param code
     * @return
     */
    public String recoverIdentifiers(String code) {
        for (int i = 0; i < hashTableMarkers.size(); i++) {
            code = code.replace(hashTableConvert.get(i), hashTableMarkers.get(i));
        }
        return code;
    }
}

/*\n
newline
\t
tab
\b
backspace
\f
form feed
\r
return
\"
"   (double quote)
\'
'    (single quote)
\\
\    (back slash)
\uDDDD*/
/*
 * LISTA DE BUGS
 * -1 Problema ao remover comentario no caso de """
 * -2 Problema para executar removercomentario em arquivos grandes
 * -3 Erro em identificar escopo quando existe um metodo sem identificador do tipo String metodo(int i){}
 * ele declara o i como sendo do escopo anterior da hierarquia.
 *
 * 4 - ERRO "SELECT * "+
"FROM analizadorlexico; soh tem 1 mais; CORRIGIDO POREM GEROU ERRO ABAIXO
 * 5-  ERRO  "SELECT * "+get("STRING")
+"FROM analizadorlexico;
 *
 *
 * 
 */
