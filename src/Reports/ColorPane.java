package Reports;

import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * 
 * @author Helder
 */
public class ColorPane extends JTextPane {
        static LinkedList<String> busca= new LinkedList();

        /**
         * 
         * @param c
         * @param s
         */
        public void  append(Color c, String s) {  // Aplica a cor no texto
                      // StyleContext
    StyleContext sc = StyleContext.getDefaultStyleContext();
    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
    StyleConstants.Foreground, c);
    int len = getDocument().getLength(); // mesmo valor que
                                         // getText().length();
    setCaretPosition(len); // coloca o retorno no fim(sem selecao)
    setCharacterAttributes(aset, false);
    replaceSelection(s);// aqui nao a selecao, entao insere no retorno
  }

        /**
         * 
         * @param pane
         * @param TextoRestante
         * @param tokens
         * @return
         */
        public LinkedList executar(ColorPane pane, String TextoRestante,LinkedList tokens) {

     String palavra= null;
     String pintaPreto=null;
     int[] proximo= {(0),(0)};
     LinkedList posicoes = new LinkedList();
     int tipo =0;

     while(!TextoRestante.isEmpty()){       //Enquanto houver texto ele ira procurar as palavras reservadas e pinta em ordem
            proximo = proximapalavra(TextoRestante,tokens); //busca a primeira palavra reservada no texto
            pintaPreto = (String) TextoRestante.subSequence(0,proximo[0]); // pinta tudo o q houver antes
            pane.append(Color.black, pintaPreto);
            posicoes.addLast(proximo[0]);

            String analisaChave=null;
            analisaChave= (String) TextoRestante.subSequence(proximo[0],(proximo[0]+proximo[1]));


            //verifica quais das funcoes esta sendo utilizada esse meteodo buscar ou compilar
            if(validaToken(analisaChave)){//se for a mesma palavra da busca entao pinta vermelho
                  tipo=1;
            }else{tipo=0;
                 }

            if(tipo == 0){// funcionalidade compilar texto pinta palavras reservadas
                    palavra= (String) TextoRestante.subSequence(proximo[0],(proximo[0]+proximo[1]));
                    pane.append(Color.blue,palavra);
                    TextoRestante= TextoRestante.substring(proximo[0]+proximo[1],TextoRestante.length());

                    }else if(tipo == 1){// funcionalidade buscar chave pinta as palavras encontradas
                                palavra= (String) TextoRestante.subSequence(proximo[0],(proximo[0]+proximo[1]));
                                pane.append(Color.red,palavra);
                                TextoRestante= TextoRestante.substring(proximo[0]+proximo[1],TextoRestante.length());
                        
                    }else{pane.append(Color.black,palavra);//pinta palavras nao chave nem reservadas
                          TextoRestante= TextoRestante.substring(proximo[0]+proximo[1],TextoRestante.length());
                         }
        }
        

        JFrame f = new JFrame("ANALIZADOR LEXICO"); //cria janela e mostra resultado
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.setContentPane(new JScrollPane(pane));
        f.setSize(800, 600);
        f.setVisible(true);
        f.setVisible(false);
        
        return posicoes;
  }
  /**
   * 
   * @param token
   * @return
   */
  public boolean validaToken(String token){
        boolean tokenValido=false;
        for(int i=0; i<busca.size();i++){
            if(token.equals(busca.get(i))){
                tokenValido=true;
            }

        }
        return tokenValido;
    }
    static void addBusca(String token){
        busca.addLast(token);
    }
    private int[] proximapalavra(String texto,LinkedList tokens) { //busca a primeira palavra reservada do texto
        int primeiraOcorrencia= 1000000000;
        int[] proximo = {(0),(0)};
        int aux = -1;
        int tamanho=0;
 
        boolean letra = false;
        String tamanhoDaPalavra = "";
        int tamanhoTotal =0;
     
        for (int i=0;i<tokens.size();i++ ){
            //fazer melhoria verificar se tem token no texto se nao tiver remover da lista para economizar
            //recurso.
            aux = texto.indexOf((String) tokens.get(i));// verifica se a palavra reservada esta no texto
           
            if (aux != -1){
                    tamanhoDaPalavra = (String) tokens.get(i);//armazena palavra encontrada
            }
            tamanhoTotal = aux+tamanhoDaPalavra.length();//pega posicao total no texto
            int cont=0;
                do{
                    letra = false;

                    if (aux!=-1){
                        //System.out.println(aux+" "+TextoRestante.length()+" "+tamanhoTotal+"ss");
                        letra = verificaToken(aux,texto,tamanhoTotal);//verifica se tem letra antes ou depois do token

                        if (letra==true){
                            cont =aux;// se tiver letra ignora a busca nao eh palavra reservada exemplo prINTln;
                            aux = texto.indexOf((String) tokens.get(i),(cont+tamanhoDaPalavra.length()));
                            tamanhoTotal = aux+tamanhoDaPalavra.length();
                            //System.out.println("Entro "+tamanhoDaPalavra+ "   "+aux);
                        }
                    }else if (aux == -1){
                                letra=false;
                          }

                }while (letra == true);
            
            if ((aux < primeiraOcorrencia )&&(aux!=-1)){// verifica a primeira palavra reservada que se encontra no texto
                
                        if (letra==false){
                            primeiraOcorrencia = aux;
                            tamanho = tamanhoDaPalavra.length();
                        }
            }
       }
        if (primeiraOcorrencia==1000000000){
            primeiraOcorrencia=texto.length();
        }
        
        proximo[0]= primeiraOcorrencia; // posicao que se encontra a palavra reservada
        proximo[1]= tamanho; // soma da onde comeca mais o tamanho da palavra
  
        return proximo;
   }

    private boolean tem_letras(String letraver) { // verifica se tem letra antes e depois
        String letras="abcdefghijklmnopqrstuvxyzwABCDEFGHIJKLMNOPQRSTUVXYZW";
        boolean letra = false;

        for(int i=0; i<letras.length();i++){
            if (letras.indexOf(letraver)!=-1){
                letra = true;
            }
        }

    return letra;

    }
    // faz a verificacao do token para ver se é ou nao palavra reservada
    private boolean verificaToken(int aux ,String dados, int tamanhoTotal) {
        boolean letra = false;
        String letraAntes;
        String letraDepois;

                if (aux-1 >=0){
                    letraAntes = dados.valueOf(dados.charAt(aux-1));
                    letra= tem_letras(letraAntes);
                }
                if (tamanhoTotal<dados.length()){
                    letraDepois = dados.valueOf(dados.charAt(tamanhoTotal));
                  
                    if (letra != true){
                        letra= tem_letras(letraDepois);
                    }
                }
        return letra;
    }
  
}