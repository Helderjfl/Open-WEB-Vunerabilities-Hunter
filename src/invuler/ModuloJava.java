/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package invuler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import sun.org.mozilla.javascript.internal.ContextFactory.Listener;

/**
 *
 * @author Ro
 */
public class ModuloJava implements Serializable {


    private LinkedList<String> primitiveTypes = new LinkedList();
    /**
     * 
     */
    public static final String PROP_PRIMITIVETYPES = "primitiveTypes";
    private LinkedList<String> identifiersDisplacement = new LinkedList();
    /**
     * 
     */
    public static final String PROP_IDENTIFIERSDISPLACEMENT = "identifiersDisplacement";
    private LinkedList<String> sqlIdentifiers = new LinkedList();
    /**
     * 
     */
    public static final String PROP_SQLIDENTIFIERS = "sqlIdentifiers";
    private LinkedList<String> reservedWords = new LinkedList();
    /**
     * 
     */
    public static final String PROP_RESERVEDWORDS = "reservedWords";
    private LinkedList<String> hashTableMarkers = new LinkedList();
    /**
     * 
     */
    public static final String PROP_HASHTABLEMARKERS = "hashTableMarkers";
    private LinkedList<String> hashTableConvert = new  LinkedList();
    /**
     * 
     */
    public static final String PROP_HASHTABLECONVERT = "hashTableConvert";
    private LinkedList<String> HashTableIdentifiers = new LinkedList();
    /**
     * 
     */
    public static final String PROP_HASHTABLEIDENTIFIERS = "HashTableIdentifiers";

    /**
     * Get the value of HashTableIdentifiers
     *
     * @return the value of HashTableIdentifiers
     */
    public LinkedList<String> getHashTableIdentifiers() {
        return HashTableIdentifiers;
    }

    /**
     * Set the value of HashTableIdentifiers
     *
     * @param HashTableIdentifiers new value of HashTableIdentifiers
     */
    public void setHashTableIdentifiers(LinkedList<String> HashTableIdentifiers) {
        LinkedList<String> oldHashTableIdentifiers = this.HashTableIdentifiers;
        this.HashTableIdentifiers = HashTableIdentifiers;
        propertyChangeSupport.firePropertyChange(PROP_HASHTABLEIDENTIFIERS, oldHashTableIdentifiers, HashTableIdentifiers);
    }

    /**
     * Get the value of hashTableConvert
     *
     * @return the value of hashTableConvert
     */
    public LinkedList<String> getHashTableConvert() {
        return hashTableConvert;
    }

    /**
     * Set the value of hashTableConvert
     *
     * @param hashTableConvert new value of hashTableConvert
     */
    public void setHashTableConvert(LinkedList<String> hashTableConvert) {
        LinkedList<String> oldHashTableConvert = this.hashTableConvert;
        this.hashTableConvert = hashTableConvert;
        propertyChangeSupport.firePropertyChange(PROP_HASHTABLECONVERT, oldHashTableConvert, hashTableConvert);
    }

    /**
     * Get the value of hashTableMarkers
     *
     * @return the value of hashTableMarkers
     */
    public LinkedList<String> getHashTableMarkers() {
        return hashTableMarkers;
    }

    /**
     * Set the value of hashTableMarkers
     *
     * @param hashTableMarkers new value of hashTableMarkers
     */
    public void setHashTableMarkers(LinkedList<String> hashTableMarkers) {
        LinkedList<String> oldHashTableMarkers = this.hashTableMarkers;
        this.hashTableMarkers = hashTableMarkers;
        propertyChangeSupport.firePropertyChange(PROP_HASHTABLEMARKERS, oldHashTableMarkers, hashTableMarkers);
    }


   
    /**
     * 
     * @param possibleVulnerabilities
     * @return
     */
    public LinkedList runSqlInjection(LinkedList<String> possibleVulnerabilities){
        for(int i=0;i<possibleVulnerabilities.size();i++){
//############# JOptionPane.showMessageDialog(null, possibleVulnerabilities.get(i));
        }
        //trata linhas com possiveis vulnerabilidades
        return possibleVulnerabilities;
    }
    /**
     * Get the value of reservedWords
     *
     * @return the value of reservedWords
     */
    public LinkedList<String> getReservedWords() {
        return reservedWords;
    }

    /**
     * Set the value of reservedWords
     *
     * @param reservedWords new value of reservedWords
     */
    public void setReservedWords(LinkedList<String> reservedWords) {
        LinkedList<String> oldReservedWords = this.reservedWords;
        this.reservedWords = reservedWords;
        propertyChangeSupport.firePropertyChange(PROP_RESERVEDWORDS, oldReservedWords, reservedWords);
    }

    /**
     * Get the value of sqlIdentifiers
     *
     * @return the value of sqlIdentifiers
     */
    public LinkedList<String> getSqlIdentifiers() {
        return sqlIdentifiers;
    }

    /**
     * Set the value of sqlIdentifiers
     *
     * @param sqlIdentifiers new value of sqlIdentifiers
     */
    public void setSqlIdentifiers(LinkedList<String> sqlIdentifiers) {
        LinkedList<String> oldSqlIdentifiers = this.sqlIdentifiers;
        this.sqlIdentifiers = sqlIdentifiers;
        propertyChangeSupport.firePropertyChange(PROP_SQLIDENTIFIERS, oldSqlIdentifiers, sqlIdentifiers);
    }


    /**
     * 
     * @return
     */
    public LinkedList<String> getIdentifiersDisplacement() {
        return identifiersDisplacement;
    }

    /**
     * Set the value of identifiersDisplacement
     *
     * @param identifiersDisplacement new value of identifiersDisplacement
     */
    public void setIdentifiersDisplacement(LinkedList<String> identifiersDisplacement) {
        LinkedList<String> oldIdentifiersDisplacement = this.identifiersDisplacement;
        this.identifiersDisplacement = identifiersDisplacement;
        propertyChangeSupport.firePropertyChange(PROP_IDENTIFIERSDISPLACEMENT, oldIdentifiersDisplacement, identifiersDisplacement);
    }
    private VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    /**
     * Add VetoableChangeListener.
     *
     * @param listener
     */
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    /**
     * Remove VetoableChangeListener.
     *
     * @param listener
     */
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }



    /**
     * 
     * @return
     */
    public LinkedList<String> getPrimitiveTypes() {
        return primitiveTypes;
    }

    /**
     * Assign the values to the primitive types (String, int, long,...), Reserved Words (private, protected, public,...)
     * HashConverter, Sql Identifiers (select, insert, delete,...) etc
     */
    public void reset(){
        LinkedList<String> aux_primitiveTypes = new LinkedList();
        aux_primitiveTypes.addLast("String");
        aux_primitiveTypes.addLast("int");
        aux_primitiveTypes.addLast("long");
        aux_primitiveTypes.addLast("double");
        aux_primitiveTypes.addLast("char");
        aux_primitiveTypes.addLast("boolean");
        aux_primitiveTypes.addLast("byte");
        aux_primitiveTypes.addLast("float");
        aux_primitiveTypes.addLast("long");
        aux_primitiveTypes.addLast("short");
        setPrimitiveTypes(aux_primitiveTypes);

        LinkedList<String> aux_identifiersDisplacement = new LinkedList();
        aux_identifiersDisplacement.addLast("for");
        aux_identifiersDisplacement.addLast("public");
        aux_identifiersDisplacement.addLast("private");
        aux_identifiersDisplacement.addLast("protected");
        aux_identifiersDisplacement.addLast("catch");
        setIdentifiersDisplacement(aux_identifiersDisplacement);


        LinkedList<String> aux_sqlIdentifiers = new LinkedList();
        aux_sqlIdentifiers.addLast("SELECT");
        aux_sqlIdentifiers.addLast("INSERT");
        aux_sqlIdentifiers.addLast("DELETE");
        aux_sqlIdentifiers.addLast("ALTER");
        aux_sqlIdentifiers.addLast("select");
        aux_sqlIdentifiers.addLast("insert");
        aux_sqlIdentifiers.addLast("delete");
        aux_sqlIdentifiers.addLast("alter");
        aux_sqlIdentifiers.addLast("UPDATE");
        aux_sqlIdentifiers.addLast("update");
        setSqlIdentifiers(aux_sqlIdentifiers);

        LinkedList<String> aux_ReservedWords = new LinkedList();
        aux_ReservedWords.addLast("private");
        aux_ReservedWords.addLast("protected");
        aux_ReservedWords.addLast("public");
        aux_ReservedWords.addLast("abstract");
        aux_ReservedWords.addLast("class");
        aux_ReservedWords.addLast("extends");
        aux_ReservedWords.addLast("implements");
        aux_ReservedWords.addLast("interface");
        aux_ReservedWords.addLast("native");
        aux_ReservedWords.addLast("new");
        aux_ReservedWords.addLast("static");
        aux_ReservedWords.addLast("strictfp");
        aux_ReservedWords.addLast("synchronized");
        aux_ReservedWords.addLast("transient");
        aux_ReservedWords.addLast("volatile");
        aux_ReservedWords.addLast("break");
        aux_ReservedWords.addLast("case");
        aux_ReservedWords.addLast("continue");
        aux_ReservedWords.addLast("default");
        aux_ReservedWords.addLast("do");
        aux_ReservedWords.addLast("else");
        aux_ReservedWords.addLast("for");
        aux_ReservedWords.addLast("if");
        aux_ReservedWords.addLast("instanceof");
        aux_ReservedWords.addLast("return");
        aux_ReservedWords.addLast("switch");
        aux_ReservedWords.addLast("while");
        aux_ReservedWords.addLast("assert");
        aux_ReservedWords.addLast("catch");
        aux_ReservedWords.addLast("finally");
        aux_ReservedWords.addLast("throw");
        aux_ReservedWords.addLast("throws");
        aux_ReservedWords.addLast("try");
        aux_ReservedWords.addLast("import");
        aux_ReservedWords.addLast("package");
        aux_ReservedWords.addLast("boolean");
        aux_ReservedWords.addLast("byte");
        aux_ReservedWords.addLast("char");
        aux_ReservedWords.addLast("double");
        aux_ReservedWords.addLast("float");
        aux_ReservedWords.addLast("int");
        aux_ReservedWords.addLast("long");
        aux_ReservedWords.addLast("short");
        aux_ReservedWords.addLast("super");
        aux_ReservedWords.addLast("this");
        aux_ReservedWords.addLast("void");
        aux_ReservedWords.addLast("const");
        aux_ReservedWords.addLast("goto");

        setReservedWords(aux_ReservedWords);

        LinkedList<String> aux_HashTableMarkers = new LinkedList();
        aux_HashTableMarkers.addLast("for");  //0
        aux_HashTableMarkers.addLast("public");//1
        aux_HashTableMarkers.addLast("private");//2
        aux_HashTableMarkers.addLast("protected");//3
        aux_HashTableMarkers.addLast("{");//4
        aux_HashTableMarkers.addLast("}");//5

        aux_HashTableMarkers.addLast(";");//6
        aux_HashTableMarkers.addLast(")");//7
        aux_HashTableMarkers.addLast("(");//8
        aux_HashTableMarkers.addLast(",");//9
        aux_HashTableMarkers.addLast("\"");//10
        aux_HashTableMarkers.addLast("'");//11

        aux_HashTableMarkers.addLast("String");//12
        aux_HashTableMarkers.addLast("int");//13
        aux_HashTableMarkers.addLast("long");//14
        aux_HashTableMarkers.addLast("double");//15
        aux_HashTableMarkers.addLast("char");//16
        aux_HashTableMarkers.addLast("boolean");//17
        aux_HashTableMarkers.addLast("byte");//18
        aux_HashTableMarkers.addLast("float");//19
        aux_HashTableMarkers.addLast("long");//20
        aux_HashTableMarkers.addLast("short");//21
        aux_HashTableMarkers.addLast("float");//22
        aux_HashTableMarkers.addLast("//");//23
        aux_HashTableMarkers.addLast("/*");//24
        aux_HashTableMarkers.addLast("*/");//25
        aux_HashTableMarkers.addLast("class");//26
        setHashTableMarkers(aux_HashTableMarkers);


        
        LinkedList<String> aux_HashTableConvert = new LinkedList();
        int stringConvert = "for".hashCode();//0
        String converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "public".hashCode();//1
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "private".hashCode();//2
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "protected".hashCode();//3
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "{".hashCode();//4
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "}".hashCode();//5
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = ";".hashCode();//6
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "(".hashCode();//7
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = ")".hashCode();//8
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = ",".hashCode();//9
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "\"".hashCode();//10
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "'".hashCode();//11
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "String".hashCode();//12
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "int".hashCode();//13
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "long".hashCode();//14
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "double".hashCode();//15
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "char".hashCode();//16
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "boolean".hashCode();//17
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "byte".hashCode();//18
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "float".hashCode();//19
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "long".hashCode();//20
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "short".hashCode();//21
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "float".hashCode();//22
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "//".hashCode();//23
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "/*".hashCode();//24
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "*/".hashCode();//25
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        stringConvert = "class".hashCode();//26
        converted = String.valueOf("@"+stringConvert+"@");
        aux_HashTableConvert.addLast(converted);
        setHashTableConvert(aux_HashTableConvert);

        LinkedList<String> aux_HashTableIdentifiers = new LinkedList();
        stringConvert = "for".hashCode();
        converted = String.valueOf("%"+stringConvert+"%");
        aux_HashTableIdentifiers.addLast(converted);
        stringConvert = "public".hashCode();
        converted = String.valueOf("%"+stringConvert+"%");
        aux_HashTableIdentifiers.addLast(converted);
        stringConvert = "private".hashCode();
        converted = String.valueOf("%"+stringConvert+"%");
        aux_HashTableIdentifiers.addLast(converted);
        stringConvert = "protected".hashCode();
        converted = String.valueOf("%"+stringConvert+"%");
        aux_HashTableIdentifiers.addLast(converted);
        stringConvert = "catch".hashCode();
        converted = String.valueOf("%"+stringConvert+"%");
        aux_HashTableIdentifiers.addLast(converted);

        setHashTableIdentifiers(aux_HashTableIdentifiers);
    }
    /**
     * 
     * @param primitiveTypes
     */
    public void setPrimitiveTypes(LinkedList<String> primitiveTypes) {
        LinkedList<String> oldPrimitiveTypes = this.primitiveTypes;
        this.primitiveTypes = primitiveTypes;
        propertyChangeSupport.firePropertyChange(PROP_PRIMITIVETYPES, oldPrimitiveTypes, primitiveTypes);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
