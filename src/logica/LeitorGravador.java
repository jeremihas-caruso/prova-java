package src.logica;

import src.estrutura.Aluno;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List; //Foi escolhido o tipo lista por manter a ordem dos elementos e ser de fácil iteração em um laço

public class LeitorGravador extends DefaultHandler { //Para usar o parser SAX, a classe precisa extender o DefaultHandler

    //Attributes
    private List<Aluno> alunos;

    //Constructor
    public LeitorGravador () {
        alunos = new ArrayList<>();
    }

    //Methods
    public List<Aluno> lerAlunosDoArquivoXml(String filename) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(filename, this);
        } catch (ParserConfigurationException e) {
            System.out.println("Erro ao configurar o parser XML");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("Erro ao parserar o arquivo XML, o arquivo é um XML válido?");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado!");
            e.printStackTrace();
        }

        return alunos;
    }

    //SAX methods
    public void startElement (String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("aluno")) {
            Aluno aluno = new Aluno(); //Esse hardcode para popular os atributos do aluno é horrível, mas para um programa dessa escala e pelo tempo disponível dá pra passar
            aluno.setId(attributes.getValue(0));
            aluno.setNome(attributes.getValue(1));
            aluno.setMedia(Float.parseFloat(attributes.getValue(2)));
            alunos.add(aluno);
        }
    }

    public void backup( String arquivoOriginal, String arquivoBackup ) {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(arquivoOriginal)); //Lê o arquivo original como texto plano por meio de um buffer
            try {
                StringBuilder buider = new StringBuilder();
                String line = buffer.readLine();

                while (line != null) {
                    buider.append(line);
                    buider.append(System.lineSeparator());
                    line = buffer.readLine();
                }
                String conteudo = buider.toString();
                save (arquivoBackup, conteudo);
            } finally {
                buffer.close();
            }
        } catch (IOException e) {
            System.out.println("O arquivo original não foi encontrado!");
            e.printStackTrace();
        }
    }

    public static void atualizarResumo( String filename, float mediaGeral, float notaMinima, float notaMaxima, int quantidadeDeAlunos ) throws Exception {
    }

    //Salva um novo arquivo à partir de uma String
    private void save (String nomeDoArquivo, String conteudo) {
        try {
            PrintWriter printWriter = new PrintWriter(nomeDoArquivo + ".xml");
            printWriter.print(conteudo);
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
