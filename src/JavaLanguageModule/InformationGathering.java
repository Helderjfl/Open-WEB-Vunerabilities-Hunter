/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaLanguageModule;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Helder
 */
public class InformationGathering{
    LinkedList<vulnerable> vulnerable = new LinkedList();
    
    /** It adds at the variablesScope all the code lines with SQL commands related with the variable.
     * 
     * @param sendToTheModule
     * @param variablesScope
     * @param scope
     * @param listHierarchyOfScopes
     * @param sqlIdentifier
     */
    public void sendToTheModuleScopes(LinkedList<String> sendToTheModule, LinkedList<variableInfo> variablesScope, LinkedList<scopeInfo> scope, LinkedList<String> listHierarchyOfScopes, LinkedList<String> sqlIdentifier) {
        for (int i=0;i < sendToTheModule.size();i++)
            for (int j=0;j < variablesScope.size();j++){
                if (sendToTheModule.get(i).contains(variablesScope.get(j).name)){
                    //precisa generalizar essa função para pegar qualquer comando sql select/update/delete/
                    for (int k=0;k < sqlIdentifier.size(); k++)
                        if (sendToTheModule.get(i).contains(sqlIdentifier.get(k)))
                        {
                            variablesScope.get(j).sqlScope =  sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("[")+1, sendToTheModule.get(i).indexOf("]"));
                            variablesScope.get(j).sqlCode = sendToTheModule.get(i);
                        }
                    scopeInfo aux = new scopeInfo();
                    aux.scope = (sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("[")+1, sendToTheModule.get(i).indexOf("]")));
                    aux.numLine = Integer.parseInt(sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("]")+1, sendToTheModule.get(i).indexOf("##")));
                    scope.add(aux);
                }
            }

        for (int i=0;i < scope.size();i++)
            System.out.println(scope.get(i).scope);


        for (int i=0;i < scope.size();i++){
            for (int j=0; j < listHierarchyOfScopes.size();j++){
                if (listHierarchyOfScopes.get(j).contains("[" + scope.get(i).scope + "]"))
                    scope.get(i).scope = listHierarchyOfScopes.get(j);
            }
        }
        
        for (int i=0;i < scope.size();i++)
            System.out.println(scope.get(i).scope);
    }
    
    /**
     *  Idenfifies the variable scope.
     * 
     * @param sqlInfo
     * @param scope
     * @param listOfMethods
     * @param variableScope
     * @param variables
     */
    public void identifyVariableScope(LinkedList<sqlInfo> sqlInfo, LinkedList<scopeInfo> scope, LinkedList<String> listOfMethods, LinkedList<variableInfo> variableScope, LinkedList<String> variables) {
        
        for (int m=0;m < sqlInfo.size(); m++){
            for (int i=0;i < sqlInfo.size();i++){
                String[] scopeSplitted = sqlInfo.get(i).sqlScope.split(",");

                for (int j=1;j < scopeSplitted.length;j++){
                    for (int k=0;k < listOfMethods.size();k++){

                        if (listOfMethods.get(k).contains(scopeSplitted[j]))
                            for (int l=0;l < sqlInfo.get(i).variablesScope.size(); l++)

                                if (sqlInfo.get(i).variablesScope.get(l).scope == null)
                                    if (listOfMethods.get(k).contains(sqlInfo.get(i).variablesScope.get(l).name)){
                                        sqlInfo.get(i).variablesScope.get(l).scope = scopeSplitted[j];
                                    }
                    }

                    for (int k=0;k < variables.size();k++){

                        if (variables.get(k).contains(scopeSplitted[j]))
                            for (int l=0;l < sqlInfo.get(i).variablesScope.size(); l++)

                                if (sqlInfo.get(i).variablesScope.get(l).scope == null)
                                    if (variables.get(k).contains(sqlInfo.get(i).variablesScope.get(l).name)){
                                        sqlInfo.get(i).variablesScope.get(l).scope = scopeSplitted[j];
                                    }
                    }
                }
            }
        }
        
        /*for (int i=0;i < scope.size();i++){
            String[] scopeSplitted = scope.get(i).scope.split(",");

            for (int j=1;j < scopeSplitted.length;j++){
                for (int k=0;k < listOfMethods.size();k++){
                    
                    if (listOfMethods.get(k).contains(scopeSplitted[j]))
                        for (int l=0;l < variableScope.size(); l++)
                            
                            if (variableScope.get(l).scope == null)
                                if (listOfMethods.get(k).contains(variableScope.get(l).name)){
                                    variableScope.get(l).scope = scopeSplitted[j];
                                }
                }
                
                for (int k=0;k < variables.size();k++){
      
                    if (variables.get(k).contains(scopeSplitted[j]))
                        for (int l=0;l < variableScope.size(); l++)
                            
                            if (variableScope.get(l).scope == null)
                                if (variables.get(k).contains(variableScope.get(l).name)){
                                    variableScope.get(l).scope = scopeSplitted[j];
                                }
                }
            }
        }*/
    }
    
    /**
     * Gets all the code of the variable hierarchy scope.
     * @param variablesScope
     * @param listHierarchyOfScopes
     * @param lineToCheck
     * @param codeForItScope
     */
    public void variablesScopeCode (LinkedList<variableInfo> variablesScope, 
            LinkedList<String> listHierarchyOfScopes, LinkedList<String> lineToCheck, LinkedList<String> codeForItScope){
        LinkedList<String> scope = new LinkedList();
        //Pega a hierarquia de escopo da linha que será verificada para que se possa pegar mais informações
        for (int i=0;i < variablesScope.size();i++){
            for (int j=0; j < listHierarchyOfScopes.size();j++){
                //if (listHierarchyOfScopes.get(j).contains(lineToCheck.get(i).substring(lineToCheck.get(i).indexOf("["), lineToCheck.get(i).indexOf("]"))))
                if (listHierarchyOfScopes.get(j).contains('[' + variablesScope.get(i).scope + ']'))
                    scope.add(listHierarchyOfScopes.get(j));
            }
        }
        //String[] scopeList = null;
        for (int i=0;i < scope.size();i++){
            //System.out.println(scope.get(i));
            String[] scopeList =  scope.get(i).split(",");

        //Junta o código dos escopos que serão analisados
            String partCode = "";
            for (int j=0;j < codeForItScope.size();j++){
                for (int k=1;k < scopeList.length;k++)
                if (codeForItScope.get(j).contains(scopeList[k]))
                    partCode = partCode + "\n" + codeForItScope.get(j);
                    /*if (i < partCode.size())
                        partCode.set(i, partCode.get(i) + "\n" + codeForItScope.get(j));
                    else
                        partCode.add(codeForItScope.get(j));*/
            }
            variablesScope.get(i).code = partCode;
        }
        //apenas verificando o conteúdo, pode ser apagado depois
        //for (int i=0;i < partCode.size();i++)
        //    System.out.println(partCode.get(i));
        
        for (int i = 0;i < variablesScope.size();i++){
            System.out.print(variablesScope.get(i).name);
            System.out.println("   " + variablesScope.get(i).scope + "\n");
            System.out.println(variablesScope.get(i).code);
            
            System.out.println("___________________________________________\n\n\n");
            
        }

        /*for (int i=0;i < modifiedVariables.size();i++){
            for (int j=0;j < variables.size();j++){
                if (modifiedVariables.get(i).matches(variables.get(j).substring(variables.get(j).indexOf("|")+1, variables.get(j).indexOf("@")))){
                    System.out.println(modifiedVariables.get(i));
                    System.out.println(variables.get(j).substring(variables.get(j).indexOf("|")+1, variables.get(j).indexOf("@")));
                }
            }
        }*/

    }
    
    /**
     * 
     * @param variablesScope
     * @param codeForItScope
     * @param listHierarchyOfScopes
     */
    public void completeScope (LinkedList<variableInfo> variablesScope, LinkedList<String> codeForItScope, LinkedList<String> listHierarchyOfScopes){
        LinkedList<String> scopePile = new LinkedList();
        for (int i = 0; i < variablesScope.size(); i++){
            scopePile.add(variablesScope.get(i).scope);
            
            while (!scopePile.isEmpty()){
                
            }
            
            for (int k = 0; k < listHierarchyOfScopes.size(); k++){
                if (listHierarchyOfScopes.get(k).contains(variablesScope.get(i).scope) && 
                    !listHierarchyOfScopes.get(i).substring(listHierarchyOfScopes.get(i).indexOf("["), listHierarchyOfScopes.get(i).indexOf("]")).matches(variablesScope.get(i).scope)){
                    
                }
                    
            }
            //variablesScope.get(i).scope;
            for (int j = 0; j < codeForItScope.size(); j++){
                
            }
        }
        
        for (int i = 0; i < codeForItScope.size(); i++){
            for (int j = 0; j < variablesScope.size(); j++){
                
            }
        }
    }
    
    /**
     * Search for protection at the code for the specific variable that were used at a SQL command.
     * @param sqlInfo
     * @param variablesScope
     * @param codeForItScope
     * @param listHierarchyOfScopes
     * @param listOfMethods
     * @param variables
     */
    public void backWayScope (LinkedList<sqlInfo> sqlInfo, LinkedList<variableInfo> variablesScope, LinkedList<String> codeForItScope, LinkedList<String> listHierarchyOfScopes, 
            LinkedList<String> listOfMethods, LinkedList<String> variables){
        
        //pega o escopo inicial
        getScope(sqlInfo, codeForItScope,variablesScope, listHierarchyOfScopes);
        //completa o escopo com base no caminho da variável
        completeBackWayScope(sqlInfo, variablesScope, codeForItScope, listHierarchyOfScopes, listOfMethods, variables);

    }
    
    /**
     * Gets the code for it variable scope.
     * @param sqlInfo
     * @param codeForItScope
     * @param variablesScope
     * @param listHierarchyOfScopes
     */
    public void getScope(LinkedList<sqlInfo> sqlInfo, LinkedList<String> codeForItScope, LinkedList<variableInfo> variablesScope, LinkedList<String> listHierarchyOfScopes){
        //pega o código referente ao escopo
        /*for (int i=0;i < variablesScope.size();i++){
            String partCode = "";
            for (int j=0;j < codeForItScope.size();j++){
                //for (int k=1;k < scopeList.length;k++)
                if (codeForItScope.get(j).contains("[H1E3T3]"))
                    partCode = partCode + "\n" + codeForItScope.get(j);
                    /*if (i < partCode.size())
                        partCode.set(i, partCode.get(i) + "\n" + codeForItScope.get(j));
                    else
                        partCode.add(codeForItScope.get(j));*/
       //     }

            //variablesScope.get(i).scopeCompleted.add(partCode);
       // }*/
        
        //Pega a hierarquia de escopo da linha que será verificada para que se possa pegar mais informações
        for (int l=0; l < sqlInfo.size(); l++){
            for (int i=0;i < sqlInfo.get(l).variablesScope.size();i++){
                for (int j=0; j < listHierarchyOfScopes.size();j++){
                    if (listHierarchyOfScopes.get(j).contains("[" + sqlInfo.get(l).variablesScope.get(i).sqlScope + "]"))
                        sqlInfo.get(l).variablesScope.get(i).scope = listHierarchyOfScopes.get(j);
                        //scope.add(listHierarchyOfScopes.get(j));
                }
            }
        }
        
        ////preciso modificar essa parte depois, para pegar o escopo automaticamente
        //for (int i=0)
        
        for (int l=0;l < sqlInfo.size(); l++){
            for (int i=0;i < sqlInfo.get(l).variablesScope.size();i++){
                String[] scopeList =  sqlInfo.get(l).variablesScope.get(i).scope.split(",");

            //Junta o código dos escopos que serão analisados
                String partCode = "";
                for (int j=0;j < codeForItScope.size();j++){
                    for (int k=1;k < scopeList.length;k++)
                    if (codeForItScope.get(j).contains(scopeList[k]))
                        //partCode = partCode + "\n" + codeForItScope.get(j);
                        sqlInfo.get(l).variablesScope.get(i).scopeCompleted.add(codeForItScope.get(j));
                }
                //variablesScope.get(i).scopeCompleted.add(partCode);
            }
        }
        
        //////Parte antiormente usada
        /*
        LinkedList<String> scope = new LinkedList();
        //Pega a hierarquia de escopo da linha que será verificada para que se possa pegar mais informações
        for (int i=0;i < variablesScope.size();i++){
            for (int j=0; j < listHierarchyOfScopes.size();j++){
                if (listHierarchyOfScopes.get(j).contains("[" + variablesScope.get(i).sqlScope + "]"))
                    variablesScope.get(i).scope = listHierarchyOfScopes.get(j);
                    //scope.add(listHierarchyOfScopes.get(j));
            }
        }
        
        ////preciso modificar essa parte depois, para pegar o escopo automaticamente
        //for (int i=0)
        
        for (int i=0;i < variablesScope.size();i++){
            String[] scopeList =  variablesScope.get(i).scope.split(",");

        //Junta o código dos escopos que serão analisados
            String partCode = "";
            for (int j=0;j < codeForItScope.size();j++){
                for (int k=1;k < scopeList.length;k++)
                if (codeForItScope.get(j).contains(scopeList[k]))
                    //partCode = partCode + "\n" + codeForItScope.get(j);
                    variablesScope.get(i).scopeCompleted.add(codeForItScope.get(j));

            }
            //variablesScope.get(i).scopeCompleted.add(partCode);
        }*/
    }
    
    /**
     * Complete the variable scope by find the variable way and try to find any protection
     * @param sqlInfo
     * @param variablesScope
     * @param codeForItScope
     * @param listHierarchyOfScopes
     * @param listOfMethods
     * @param variables
     */
    public void completeBackWayScope(LinkedList<sqlInfo> sqlInfo, LinkedList<variableInfo> variablesScope, LinkedList<String> codeForItScope, LinkedList<String> listHierarchyOfScopes, 
            LinkedList<String> listOfMethods, LinkedList<String> variables){
        boolean found = false;
        
        for (int m=0; m < sqlInfo.size(); m++){
            for (int i=0;i < sqlInfo.get(m).variablesScope.size();i++){
                String[] scopeList =  sqlInfo.get(m).variablesScope.get(i).scope.split(",");

                //procura pelo local onde a variável foi instanciada
                for (int j=1; j < scopeList.length;j++){
                    for (int k = 0; k < variables.size();k++)
                        if (variables.get(k).contains(scopeList[j]) && variables.get(k).contains(sqlInfo.get(m).variablesScope.get(i).name)){
                            found = true;

                            System.out.println(sqlInfo.get(m).variablesScope.get(i).scopeCompleted.size());
                            for (int l=0;l < sqlInfo.get(m).variablesScope.get(i).scopeCompleted.size();l++){
                                //verifica se há filtro no código e se está sendo usado na variável

                                found = patternSearch(sqlInfo.get(m).variablesScope.get(i).name,sqlInfo.get(m).variablesScope.get(i).scopeCompleted.get(l));
                                if (found)
                                    l=sqlInfo.get(m).variablesScope.get(i).scopeCompleted.size();

                            }
                            boolean discover = discoverIfVariableWasPassedOnMethod(sqlInfo.get(m).variablesScope,listOfMethods,i);
                            if (!found & discover)
                                    auxBackWayScope(sqlInfo.get(m).variablesScope, codeForItScope, listOfMethods, variables, i, listHierarchyOfScopes);

                            if (!found & !discover){
                                vulnerable aux = new vulnerable();
                                aux.variavel = sqlInfo.get(m).variablesScope.get(i).name;
                                //aux.sqlCode = sqlInfo.get(m).variablesScope.get(i).sqlCode;
                                aux.sqlCode = sqlInfo.get(m).slqCode;
                                aux.method = "metodo"; //estou jogando o método errado, pegar o nome do método que está chamando o método com o comando sql
                                aux.line = 0;
                                
                                if (sqlInfo.get(m).slqCode.contains("INSERT") || sqlInfo.get(m).slqCode.contains("insert"))
                                    //aux.commentary = "Não foi encontrado nenhuma proteção na variável " + sqlInfo.get(m).variablesScope.get(i).name + " utilizada no comando SQL \"" + sqlInfo.get(m).slqCode + "\", tornando-o vulnerável a ataque de injeção SQL e XSS.";                                    
                                    aux.commentary = "Não foi encontrado nenhuma proteção na variável " + sqlInfo.get(m).variablesScope.get(i).name + " utilizada no comando SQL, tornando-o vulnerável a ataque de injeção SQL e XSS.";                                    
                                else
                                    //aux.commentary = "Não foi encontrado nenhuma proteção na variável " + sqlInfo.get(m).variablesScope.get(i).name + " utilizada no comando SQL \"" + sqlInfo.get(m).slqCode + "\", tornando-o vulnerável a ataque de injeção SQL.";
                                    aux.commentary = "Não foi encontrado nenhuma proteção na variável " + sqlInfo.get(m).variablesScope.get(i).name + " utilizada no comando SQL, tornando-o vulnerável a ataque de injeção SQL.";
                                vulnerable.add(aux);
                                found = false;
                                
                            }
                            j = scopeList.length;
                            k = variables.size();
                        }
                }
                /*adiciono os escopos do caminhoa numa linkedlist da variável, caso encontre o início dela, fica guardado, caso contrário, 
                apaga para possuir apenas o caminho conseguido no método da busca de caminho através de métodos*/

                System.out.println(found);
                //if (!found){
                    /*não tendo encontrado onde foi criado a variável, é feito a busca por caminho, através dos métodos, buscando ver onde os 
                    métodos foram chamados, para continuar a busca pelo inicio da variável*/
                //    auxBackWayScope(variablesScope, codeForItScope, listOfMethods, variables, i, listHierarchyOfScopes);
                //}
                found=false;
            }  
        }
    }
    
    /* buscar o código que chama a função, pegar o número do seu escopo e buscar em todos os códigos referente a ele pela função de filtro,
     * posteriormente caso não encontre o filtro, pegar na hierarquia de escopos, sua hierarquia para procurar pelo filtro, caso não seja encontrado, pegar o nome da função e buscar no código.
     * Fazer isso até que seja encontrado um filtro ou não ser encontrado uma chamada a função. */
    
    /**
     * Part of completeBackWayScope
     * @param variablesScope
     * @param codeForItScope
     * @param listOfMethods
     * @param variables
     * @param i
     * @param listHierarchyOfScopes
     */
    public void auxBackWayScope (LinkedList<variableInfo> variablesScope, LinkedList<String> codeForItScope, 
            LinkedList<String> listOfMethods, LinkedList<String> variables, int i, LinkedList<String> listHierarchyOfScopes)
    {
        boolean found = false;
        String[] scopeList =  variablesScope.get(i).scope.split(",");
        for (int j=1;j < scopeList.length;j++)
        {
            for (int k=0;k < listOfMethods.size();k++)
            {
                if (listOfMethods.get(k).contains(scopeList[j]))
                {
                    //found = true;
                    String method = listOfMethods.get(k).substring(listOfMethods.get(k).indexOf(" ")+1, listOfMethods.get(k).indexOf("("));
                    method = method.substring(method.indexOf(" ")+1);
                    System.out.println(method);
                    
                    for (int l=0;l < codeForItScope.size();l++)
                    {
                        //verifica se o método é chamado em alguma parte do código, e verifica se ele já não está incluso no escopo já verificado
                        if (codeForItScope.get(l).contains(method + "(") && !codeForItScope.get(l).contains(scopeList[j]))
                        {
                            //separa o escopo, para então pegar toda a sua hierarquia e adicionar no escopo da variável, mas tirando os escopos já existentes.
                            String auxScope = codeForItScope.get(l).substring(codeForItScope.get(l).indexOf("[")+1, codeForItScope.get(l).indexOf("]"));
                            
                            
                            if (!variablesScope.get(i).scope.contains(auxScope))
                            {
                                for (int m=0;m < listHierarchyOfScopes.size(); m++)
                                {
                                    if (listHierarchyOfScopes.get(m).contains("[" + auxScope + "]"))
                                    {
                                        String[] auxScopeList = listHierarchyOfScopes.get(m).split(",");
                                        String variable = "";
                                        
                                        for (int n=1; n < auxScopeList.length; n++)
                                        {
                                            //busca o  nome da variável usado na função que chamou a função com código SQL
                                            for (int o=0; o < codeForItScope.size(); o++)
                                            {                      
                                                if (codeForItScope.get(o).contains("[" + auxScopeList[n] + "]")){
                                                    if (codeForItScope.get(o).contains(method))
                                                        variable = findVariable(variablesScope.get(i).name,listOfMethods.get(k),findMethodCallScopeCode(method,codeForItScope.get(o)));
                                                }
                                            }
                                            
                                                
                                        }

                                        for (int n=1; n < auxScopeList.length; n++)
                                        {
                                            //verifica se a variável está sendo filtrada nesta parte do código em todo o escopo
                                            for (int o=0; o < codeForItScope.size(); o++)
                                            {
                                                
                                                if (codeForItScope.get(o).contains("[" + auxScopeList[n] + "]")){                                        
                                                    if (patternSearch(variable, codeForItScope.get(o)))
                                                    {
                                                        System.out.println("Filtro encontrado !!!!!!!");
                                                        found = true;
                                                    }
                                                }
                                            }
                                            
                                                
                                        }
                                    }
                                }
                                
                                
                                variablesScope.get(i).scope += "," + codeForItScope.get(l).substring(codeForItScope.get(l).indexOf("[")+1, codeForItScope.get(l).indexOf("]"));
                            }
                            if (!found){
                                vulnerable aux = new vulnerable();
                                aux.variavel = variablesScope.get(i).name;
                                aux.sqlCode = variablesScope.get(i).sqlCode;
                                aux.method = method; //estou jogando o método errado, pegar o nome do método que está chamando o método com o comando sql
                                aux.commentary = "Não foi encontrado nenhuma proteção na variável " + variablesScope.get(i).name + " utilizada no comando SQL, quando chamado pelo método " + aux.method;
                                aux.line = 0;
                                vulnerable.add(aux);
                                found = false;
                            }
                        }
                    }

                }
            }
        }
    }
    
    /**
     * Search for pattern protection
     * @param variable
     * @param scopeCode
     * @return
     */
    public boolean patternSearch (String variable, String scopeCode)
    {
        int aux = scopeCode.indexOf("matcher");
        while (aux != -1)
        {
            scopeCode = scopeCode.substring(aux+8);
            //scopeCode = scopeCode.substring(0,scopeCode.substring(aux+8).indexOf(")"));
            scopeCode = scopeCode.substring(0, scopeCode.indexOf(")"));
            //System.out.println();
            System.out.println(scopeCode);
            if (variable.matches(scopeCode))
            {
                System.out.println("entrou");
                return true;
            }
            
            aux = scopeCode.indexOf("matcher");
        }
        
        return false;
    }
    
    /**
     * 
     * @param variable
     * @param methodCode
     * @param callMethodCode
     * @return
     */
    public String findVariable(String variable, String methodCode, String callMethodCode)
    {
        String auxMethodCode = methodCode;
        String auxCallMethodCode = callMethodCode;
        String aux = "";
        int i = auxMethodCode.indexOf("(");
        int j = 0;
        int countComma = 0;
        boolean found = false;
        
        while (!found)
        {
            if (auxMethodCode.indexOf(",") != -1)
                j = auxMethodCode.indexOf(",");
            else 
                j = auxMethodCode.indexOf(")");
            auxMethodCode = auxMethodCode.replaceFirst(",", ".");
            
            aux = auxMethodCode.substring(i, j);
            if (aux.contains(variable))
                found = true;
            else
            {
                i = j;
                countComma++;
            }
        }
        found = false;
        i = auxCallMethodCode.indexOf("(");
        while (!found)
        {
            if (auxCallMethodCode.indexOf(",") != -1)
                j = auxCallMethodCode.indexOf(",");
            else
                j = auxCallMethodCode.indexOf(")");
            auxCallMethodCode = auxCallMethodCode.replaceFirst(",", ".");
            
            aux = auxCallMethodCode.substring(i+1, j);
            if (countComma == 0)
                found = true;
            else
            {
                i = j;
                countComma--;
            }
        }
        
        return aux;
    }
    
    /**
     * 
     * @param method
     * @param codeForItScope
     * @return
     */
    public String findMethodCallScopeCode (String method, String codeForItScope)
    {

        String code = codeForItScope;
        int aux = code.indexOf(method);
        
        while (aux > code.indexOf(";"))
            code = code.replaceFirst(";", ".");
        System.out.println(code.substring(aux, code.indexOf(";")));
        
        return code.substring(aux, code.indexOf(";"));
        
        //return "erro";
    }
    
    /**
     * Discover if the method wass passed on method or was created inside the method.
     * @param variablesScope
     * @param listOfMethods
     * @param i
     * @return
     */
    public boolean discoverIfVariableWasPassedOnMethod (LinkedList<variableInfo> variablesScope, LinkedList<String> listOfMethods, int i)
    {
        String[] scopeList =  variablesScope.get(i).scope.split(",");
        for (int j=1;j < scopeList.length;j++)
        {
            for (int k=0;k < listOfMethods.size();k++)
            {
                if (listOfMethods.get(k).contains(scopeList[j])){
                    String method = listOfMethods.get(k).substring(listOfMethods.get(k).indexOf("("), listOfMethods.get(k).indexOf(")"));
                    if (method.contains(variablesScope.get(i).name))
                        return true;
                    else
                        return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Build sqlInfo with the basic information about the variable.
     * @param sqlInfo
     * @param sendToTheModule
     * @param sqlVariables
     * @param sqlIdentifier
     * @param listHierarchyOfScopes
     */
    public void sqlKnowledgement (LinkedList<sqlInfo> sqlInfo, LinkedList<String> sendToTheModule, LinkedList<String> sqlVariables, 
            LinkedList<String> sqlIdentifier, LinkedList<String> listHierarchyOfScopes){
        for (int i=0;i < sendToTheModule.size(); i++){
            for (int j=0; j < sqlIdentifier.size(); j++){
                if (sendToTheModule.get(i).contains(sqlIdentifier.get(j))){
                    sqlInfo aux = new sqlInfo();
                    aux.slqCode = sendToTheModule.get(i);
                    aux.sqlScope = sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("[")+1, sendToTheModule.get(i).indexOf("]"));
                    aux.numLine = Integer.parseInt(sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("]")+1, sendToTheModule.get(i).indexOf("##")));
                    aux.variablesScope = new LinkedList();
                    
                    for (int k=0; k < listHierarchyOfScopes.size();k++){
                        if (listHierarchyOfScopes.get(k).contains("[" + aux.sqlScope + "]"))
                            aux.sqlScope = listHierarchyOfScopes.get(k);
                    }
                    
                    
                    for (int k=0; k < sqlVariables.size(); k++){
                        if (aux.slqCode.contains(sqlVariables.get(k))){
                            variableInfo variable = new variableInfo();
                            variable.name = sqlVariables.get(k);
                            variable.scope = null;
                            variable.sqlScope = sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("[")+1, sendToTheModule.get(i).indexOf("]"));;
                            variable.sqlCode = sendToTheModule.get(i);
                            variable.scopeCompleted = new LinkedList();
                            variable.code = null;
                            variable.numLine = 0;
                            aux.variablesScope.add(variable);
                        }       
                    }
                    sqlInfo.add(aux);
                }
            }
        }
    }
    
    /**
     * 
     * @return Return a linkedlist with the vulnerabilities found. Contains variable name, sql code, commentary.
     */
    public LinkedList<vulnerable> vulnerabilitiesFound(){
        return vulnerable;
    }
}



class vulnerable{
    public String method;
    public String sqlCode;
    public String commentary;
    public String variavel;
    public int line;
}

/*
 * Implementar a busca considerando códigos diferentes, utilizando-se da informação do escopo, onde o primeiro caracter + número identifica escopos diferentes, colocar a proteção onde é utilizado o codeForItScope
 * Implementar a separação de comandos sql e variáveis para fazer a análise corretamente, sem misturar variáveis, fazer isso criando um método para verificar quais variáveis foram utilizadas no comando sql, e colocando 
 * tanto o sql quanto as variáreis em uma lista com registros deles
 * Implementar a busca por escopos, voltando completamente no histórico de eventos, fazer isso colocando os escopos já buscados no escopo da variável, e criando um linkedlist com a hierarquia de escopos que será verificado
 * adicionando novos escopos conforme necessário nele, e utilizando a lista de escopos da variável para não haver repetições.
 */