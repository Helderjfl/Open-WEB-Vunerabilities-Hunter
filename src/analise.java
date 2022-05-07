import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class analise {

    static Connection con;
    String driver = "com.ora.jdbc.Driver";
    String dataUrl = "jdbc:db://localhost:5112/users";
    String login = "admin";// PEGAR ESSA LINHA
    int password = 123;
    LinkedList token = new LinkedList();
    String username;

    public void valida_SRV() {
        String username ="NAO PEGAR ESSA LINHA";
        login = "NAO PEGAR ESSA LINHA";
        String senha = "NAO PEGAR ESSA LINHA";
        int password = 123444;//NAO PEGAR
        select(token,login);
    }

    public void select(LinkedList tokens, String username) {
        //String username = login;
        int password = 0;
        Pattern p = Pattern.compile("(\\w|\\s)*(\\w|\\s)*");
        Matcher m = p.matcher(username);
        boolean b = m.matches();
        //System.out.print(b);
              
        //if (b){
            String senha = getSenha(password,username);
            int iUserID = -1;
            String sLoggedUser = " teste (  ; private teste ";
        if (b){
            String sql = "SELECT User_id, Username FROM USERS WHERE Username = '" +validaUsername(password,username)+ "' AND Password = '" + senha + "'";
            java.sql.Statement s;
            try {
                //if (b){
                    s = con.createStatement();

                    java.sql.ResultSet r = s.executeQuery(sql);

                    if (r.next()) {
                        iUserID = r.getInt(1);
                        sLoggedUser = r.getString(2);
                    }
                    if (iUserID >= 0) {
                        System.out.println("User logged in: " + sLoggedUser);
                    } else {
                        System.out.println("Access Denied!");
                    }
                //}
            } catch (SQLException ex) {
                Logger.getLogger(analise.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?");
            prep.setString(1, username);
            prep.setString(2, String.valueOf(password));
            prep.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(analise.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getSenha(int password, String username) {
        password = username.hashCode()+password;

        for(int i=0; i<10;i++){
            String variavelFor= "VALIDA O ESCOPO DO FOR";
            if(variavelFor.length()>10){
                String variavelIf ="ESCOPO DO IF VALIDADO";
                login="ESSA LINHA DE CODIGO NAO É RELEVANTE PARA ANALISE DE SQL INJECTION";
            }
            boolean validado= true;
            while(!validado){
                validado=false;
                for(int j=0; j<10; j++){}
                for(int j=0; j<10; j++){
                try{
                    System.out.println("Tente executar esse codigo");
                    int teste = j*999999999;
                }catch(Exception e){}
                }
            }
        }

        return String.valueOf(password);
    }

    private void getSenha(String password, String username) {
        password = username.hashCode()+password;

        for(int i=0; i<10;i++){
            String variavelFor= "VALIDA O ESCOPO DO FOR";
            if(variavelFor.length()>10){
                String variavelIf ="ESCOPO DO IF VALIDADO";
                login="ESSA LINHA DE CODIGO NAO É RELEVANTE PARA ANALISE DE SQL INJECTION";
            }
        }

    }
    private String validaUsername(int senha ,String username) {
        username = "REALIZA ALGUM TIPO DE TRATAMENTO DE ENTRADA E SAIDA";
        return username;
    }
       public static void conexao() { // Conecta o banco de dados
        try {
            Class.forName("org.gjt.mm.mysql.Driver"); //Or any other driver
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tcc",
                    "root", "root");
        } catch (Exception x) {
            System.out.println("Unable to load the driver class!");
        }
    }
       
    public void teste ()
    {
        LinkedList token = new LinkedList();
        String username = "Helder";

        Pattern p = Pattern.compile("(\\w|\\s)*(\\w|\\s)*");
        Matcher m = p.matcher(username);
        boolean b = m.matches();
        
        if (b)
            select(token, username);
    }
}
