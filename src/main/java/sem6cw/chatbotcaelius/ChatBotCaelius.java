/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package sem6cw.chatbotcaelius;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Aaditya Duggal
 */
public class ChatBotCaelius 
{
private JTextArea mainArea = new JTextArea(25,60);
    private JTextField ch_box = new JTextField(40);
    
    public static String getResponse(String s) throws URISyntaxException, IOException, InterruptedException, JSONException 
    {
        HttpClient cl = HttpClient.newHttpClient();
        String api_uri = "https://api.openai.com/v1/completions";
//        String req_body = "{\"model\":\"text-davinci-003\",\"prompt\" :\"code in java to find whether the given number is palindrome or not\"}";
        JSONObject req = new JSONObject();
        req.put("model","text-davinci-003");
        req.put("prompt", s);
        req.put("max_tokens", 2000);
//        System.out.println(req);
        String req_body = req.toString();
        String api_key = "Bearer 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";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(api_uri))
                .header("Authorization", api_key)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(req_body, StandardCharsets.UTF_8))
                .build();
        
        HttpResponse<String> resp = cl.send(request, HttpResponse.BodyHandlers.ofString());
        int respCode = resp.statusCode();
        String respBody = resp.body();
        JSONObject result = new JSONObject(respBody);
        JSONArray choicesArray = result.getJSONArray("choices");
        JSONObject choices = choicesArray.getJSONObject(0);
//        System.out.println(choices.getString("text"));
//        System.out.println("Response Body" + respBody);
////        System.out.println("Hello World!");
        return choices.getString("text");
    }

    public ChatBotCaelius() 
    {
        Font f = new Font("Serif", Font.PLAIN, 24);
        Font f1 = new Font("Serif", Font.PLAIN, 18);
        
        JScrollPane scroll = new JScrollPane(mainArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLayout(new FlowLayout());
        window.setBounds(100,100,1000, 800);
        window.setTitle("Chit and Chat");
        window.add(scroll);
        
        //For mainArea
        mainArea.setBackground(new Color(90, 90, 90));
        mainArea.setForeground(Color.white);
        mainArea.setFont(f1);
        mainArea.setLineWrap(true);
        mainArea.setWrapStyleWord(true);
        
        //For ch_box
        ch_box.setBackground(new Color(90, 90, 90));
        ch_box.setForeground(Color.white);
        ch_box.setFont(f);
       
        ch_box.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String query = ch_box.getText();
                mainArea.append("USER -> " + query + "\n");
                ch_box.setText("");
                ch_box.setEditable(true);
                String response ="";
                try {
                    response = getResponse(query);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(ChatBotCaelius.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ChatBotCaelius.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatBotCaelius.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(ChatBotCaelius.class.getName()).log(Level.SEVERE, null, ex);
                }
                mainArea.append("BOT -> " + response + "\n\n");
            }
            
        });
        
//        window.add(mainArea);
        window.add(ch_box);
    }

    public static void main(String args[]) {
       new ChatBotCaelius(); 
    }
}
