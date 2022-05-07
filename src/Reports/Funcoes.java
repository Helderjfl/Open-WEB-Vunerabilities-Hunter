package Reports;

import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.JTextPane;

/**
 * 
 * @author Helder
 */
public class Funcoes {
    ColorPane pane;

    /**
     * 
     * @return
     */
    public ColorPane getPane() {
        return pane;
    }

    /**
     * 
     * @param pane
     */
    public void setPane(ColorPane pane) {
        this.pane = pane;
    }
     
    /**
     * 
     */
    public void Funcoes(){
    }
    
    
    /**
     * 
     * @param tokens
     * @param txtTexto
     */
    public void pintatokens(LinkedList tokens,String txtTexto ){
        pane = new ColorPane();
        String teste = null;
        teste= txtTexto;
        pane.executar(pane,teste,tokens);  //chama metodo para pintar as palavras

  }

  /**
   * 
   * @param txtTexto
   * @param palavra
   * @param reservedWords
   * @return
   */
  public LinkedList procuraChave(String txtTexto, LinkedList<String> palavra,LinkedList<String> reservedWords ) {
 
        int j=0;// variáveis de controle

        String texto = txtTexto;// texto recebe o conteudo da janela

        LinkedList<String> tokens = new LinkedList();
        tokens=reservedWords;
        LinkedList  encontrouNestaLinha = new LinkedList();
        String linha[] = texto.split("\\n");//separa as linhas
        String ocorrencia;
        String Analisa="";
        int maior;
        for(int i =0;i<palavra.size();i++){
        int numeroLinha=0;          // esse treche verifica a posicao da palavra chave
                                    //e separa uma parte antes e depois para inserir na tabela com o valor centralizado
        int menor;
            for(j=0;j<linha.length;j++){
                Analisa = palavra.get(i).substring(palavra.get(i).indexOf("]")+1);
                numeroLinha= linha[j].indexOf(Analisa);
                if (numeroLinha != -1){
                    if((numeroLinha-40 > 0)){
                        if ((numeroLinha+Analisa.length()+40)<linha[j].length()){
                            maior=numeroLinha+Analisa.length()+40;
                            menor = numeroLinha-40;
                            ocorrencia = linha[j].substring(menor,maior);
                        }else{
                            maior=linha[j].length();
                            menor = numeroLinha-40;
                            ocorrencia = linha[j].substring(menor,maior);
                        }                                              
                    }else if ((numeroLinha+Analisa.length()+40)<linha[j].length()){
                            maior=numeroLinha+Analisa.length()+40;
                            menor = 0;
                            ocorrencia = linha[j].substring(menor,maior);
                           }else {
                                maior=linha[j].length();
                                menor =0;
                                ocorrencia = linha[j].substring(menor,maior);
                    }            
                    encontrouNestaLinha.addLast(j+1);
                    encontrouNestaLinha.addLast(ocorrencia);
                }
            }
        

        ColorPane.addBusca(Analisa);// define qual expressao sera buscada
        tokens.addLast(Analisa);   // adiciona a palavra na lista de tokens para realizar a analise
        }
        this.pintatokens(tokens, txtTexto);
        //## ColorPane.setBusca(null); // zera a palavra da busca
        tokens.removeLast();      //remove o token da ultima busca
        
        return encontrouNestaLinha;
    }

}

//27/3
/* Lista de BUGS
 * 1 - O ANALIZADOR BUSCA NA STRING SE A OCORRENCIA NA PALAVRA EX  PRINT ELE CONSIDERA O INT COMO PALAVRA
 * 2 - VERIFICAR SUPORTE PARA STRING DA SELEC POIS NAO ESTA CONSEGUINDO VERIFICAR EM APENAS UMA SELECT
 * TENTEI QUEBRA MAIS EXISTE PROBLEMA COM
                java.sql.Statement s = con.createStatement();
                java.sql.ResultSet r = s.executeQuery (todo);
 *  REALIZAR VERIFICACAO 16/03/2010
 *
 */

//2/4 Lista de Bugs
/* Bug 1 e 2 Corrigido
 * Trabalhando no metodo de busca de palavra chave mostrando ocorrencia das linhas
 */

//12/4
// Bugs encontrados  e Corrigidos no metodo de busca quando cancelava ou entrava sem nenhum valor

//19/04 Bug encontrado inseri um metodo para remover tokens nao utilizados para agilizar consulta porem
// esta dando problema nao esta pintando alguns token // o metodo foi removido necessario criar outro