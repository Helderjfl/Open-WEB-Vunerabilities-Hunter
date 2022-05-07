/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package JavaLanguageModule;
import java.io.Serializable;
import java.util.LinkedList;



///////////////registros////////////////
/**
 *
 * @author Helder
 */
public class JavaModuleForTreatment implements Serializable {
    InformationGathering information = new InformationGathering();

    /**
     * Main function to search for protection.
     * @param sendToTheModule The lines that have to be analyzed.
     * @param variables All the variables found on the code.
     * @param sqlVariables All the variables used at any sql command.
     * @param listOfMethods List of all the methods found on the code (including the variables that they receive).
     * @param nameOfMethods Just the name of the methods.
     * @param codeForItScope The code of it scope.
     * @param listHierarchyOfScopes The Hierarchy of every scope.
     * @param sqlIdentifier The commands that identify an sql command. (UPDATE, DELETE,...)
     */
    public void SqlInjectionSearch (LinkedList<String> sendToTheModule, LinkedList<String> variables, LinkedList<String> sqlVariables, 
            LinkedList<String> listOfMethods, LinkedList<String> nameOfMethods, LinkedList<String> codeForItScope,
            LinkedList<String> listHierarchyOfScopes, LinkedList<String> sqlIdentifier){
        System.out.println("////////////////////////////////|||||\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");

        //criação do registro com as informações das variáveis usadas no comando SQL
        LinkedList<variableInfo> variableScope = new LinkedList();
        for (int i=0;i < sqlVariables.size();i++){
            variableInfo aux = new variableInfo();
            aux.name = sqlVariables.get(i);
            aux.scope = null;
            aux.sqlScope = null;
            aux.sqlCode = null;
            aux.scopeCompleted = new LinkedList();
            aux.code = null;
            aux.numLine = 0;
            variableScope.add(aux);
        }
        LinkedList<sqlInfo> sqlInfo = new LinkedList();
        
        information.sqlKnowledgement(sqlInfo, sendToTheModule, sqlVariables, sqlIdentifier, listHierarchyOfScopes);
        LinkedList<scopeInfo> scope = new LinkedList();
        //identifica a hierarquia de escopos das linhas recebidas pelo módulo
        information.sendToTheModuleScopes(sendToTheModule, variableScope, scope, listHierarchyOfScopes, sqlIdentifier);

        for (int i=0;i < scope.size();i++)
            System.out.println(scope.get(i).scope + " - " + scope.get(i).numLine);
        //continuar descoberta de escopo para ver quais métodos do listOfMethod pode ser o inicial do comando SQL
        //verificar na lista de métodos, depois verificar seguindo a hierarquia dos escopos, em qual método ele foi criado

        

        //identifica o escopo das variáveis recebidas para verificação
        information.identifyVariableScope(sqlInfo, scope, listOfMethods, variableScope, variables);
        information.backWayScope(sqlInfo, variableScope, codeForItScope, listHierarchyOfScopes, listOfMethods, variables);

        for (int i=0; i < variableScope.size();i++)
            System.out.println(variableScope.get(i).name + " - " + variableScope.get(i).scope);

        //LinkedList<String> variableToCheck = new LinkedList();
        LinkedList<String> lineToCheck = new LinkedList();
        /*for (int i = 0;i < sqlVariables.size();i++){
            for (int j = 0;j < sendToTheModule.size();j++){
                int variableFound = sendToTheModule.get(j).indexOf(sqlVariables.get(i).toString());

                int attributionFound = sendToTheModule.get(j).indexOf("=");

                if (variableFound < attributionFound && (variableFound != -1 && attributionFound != -1)){
                    System.out.println(variableFound);
                    System.out.println(attributionFound);

                    String aux = sendToTheModule.get(j).substring(variableFound+sqlVariables.get(i).length(), attributionFound);
                    int k = 0;
                    boolean check = true;
                    while (k < aux.length()-1){
                        if (aux.charAt(k) != ' ')
                            check = false;
                        k++;
                    }

                    //usar a lista de variáveis e métodos para verificar se a variável foi modificada
                    if (check){
                        int l = 0;
                        String lineOfCodeToCheck = sendToTheModule.get(j).substring(attributionFound+1);
                        String auxCode = "";
                        check = false;
                        /*while (l < lineOfCodeToCheck.length()){
                            if (lineOfCodeToCheck.charAt(l) != ' '){
                                auxCode = auxCode + lineOfCodeToCheck.charAt(l);
                                //verifica se recebe valor de alguma variável
                                for (int m=0;m < variables.size();m++){
                                    String auxi = variables.get(m).substring(variables.get(m).indexOf("|")+1, variables.get(m).indexOf("@"));
                                    if (auxCode.equals(auxi)){
                                        check = true;
                                        m = variables.size();
                                    }
                                }

                                //verifica se recebe valor de algum método
                                for (int m=0;m < nameOfMethods.size();m++){
                                    if (auxCode.equals(nameOfMethods.get(m))){
                                        check = true;
                                        m = nameOfMethods.size();
                                    }
                                }

                                if (!check)
                                    for (int m=0;m < listOfMethods.size();m++){
                                        if (auxCode.equals(listOfMethods.get(m))){
                                            check = true;
                                            m = listOfMethods.size();
                                        }
                                    }

                                if (check){
                                    System.out.println("A variável " + sqlVariables.get(i).toString() + " pode ter sido modificada");
                                    variableToCheck.add(sqlVariables.get(i).toString());
                                    lineToCheck.add(sendToTheModule.get(j));
                                    check = false;
                                }
                            }
                            l++;

                            //////buscar qual o escopo da variável modificada no sqlVariables.
                        }
                    }
                }
            }
        }*/

        //variableToCheck = information.variablesScopeCode(variables, variableToCheck, listHierarchyOfScopes, lineToCheck, codeForItScope);
        //information.variablesScopeCode(variableScope, listHierarchyOfScopes, lineToCheck, codeForItScope);
    }

    /*private void sendToTheModuleScopes(LinkedList<String> sendToTheModule, LinkedList<String> sqlIdentifier, LinkedList<String> scope, LinkedList<String> listHierarchyOfScopes) {
        for (int i=0;i < sendToTheModule.size();i++)
            for (int j=0;j < sqlIdentifier.size();j++){
                if (sendToTheModule.get(i).contains(sqlIdentifier.get(j)))
                    scope.add(sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("[")+1, sendToTheModule.get(i).indexOf("]")));
            }

        for (int i=0;i < scope.size();i++)
            System.out.println(scope.get(i));


        for (int i=0;i < scope.size();i++){
            for (int j=0; j < listHierarchyOfScopes.size();j++){
                if (listHierarchyOfScopes.get(j).contains("[" + scope.get(i) + "]"))
                    scope.set(i,listHierarchyOfScopes.get(j));
            }
        }
    }*/

    /*public LinkedList <sqlInfo> sqlPass (LinkedList<String> sendToTheModule, LinkedList<String> sqlIdentifier){
        LinkedList <sqlInfo> sql = new LinkedList();
        for (int i=0;i < sendToTheModule.size(); i++){
            for (int j=0; j < sqlIdentifier.size(); j++){
                if (sendToTheModule.contains(sqlIdentifier.get(j))){
                    sqlInfo aux = new sqlInfo();
                    aux.slqCode = sendToTheModule.get(i);
                    aux.sqlScope = sendToTheModule.get(i).substring(sendToTheModule.get(i).indexOf("[")+1, sendToTheModule.get(i).indexOf("]"));
                    aux.variablesScope = null;
                    
                }
            }
        }
            
        return sql;
    }*/
    
    /**
     * Return a linkedlist with the vulnerabilities found. Contains variable name, sql code, commentary.
     * Doesn't work for some reason.
     * @return
     */
    public LinkedList<vulnerable> vulnerabilitiesFound(){
        return information.vulnerable;
    }
    
    /**
     * 
     * @return The Sql codes from the vulnerabilites found
     */
    public LinkedList vulnerabilitiesFound_SQLCode(){
        LinkedList aux = new LinkedList();
        
        for (int i=0; i < information.vulnerable.size();i++)
            aux.add(information.vulnerable.get(i).sqlCode);
        return aux;
    }
    
    /**
     * 
     * @return The commentary about all the vulnerabilities found
     */
    public LinkedList vulnerabilitiesFound_Commentary(){
        LinkedList aux = new LinkedList();
        
        for (int i=0; i < information.vulnerable.size();i++)
            aux.add(information.vulnerable.get(i).commentary);
        return aux;
    }
    
    /**
     * 
     * @return All the variables that are vulnerable.
     */
    public LinkedList vulnerabilitiesFound_Variable(){
        LinkedList aux = new LinkedList();
        
        for (int i=0; i < information.vulnerable.size();i++)
            aux.add(information.vulnerable.get(i).variavel);
        return aux;
    }
    
    /**
     * 
     * @return Return the method where the vulnerability were found. Not correctly implemented.
     */
    public LinkedList vulnerabilitiesFound_Method(){
        LinkedList aux = new LinkedList();
        
        for (int i=0; i < information.vulnerable.size();i++)
            aux.add(information.vulnerable.get(i).method);
        return aux;
    }
}
class variableInfo{
    public String name;
    public String scope;
    public String sqlScope;
    public String sqlCode;
    public String code;
    public LinkedList<String> scopeCompleted;
    //public String[] scopeCompleted;
    public int numLine;
}


class scopeInfo{
    public String scope;
    public int numLine;
}