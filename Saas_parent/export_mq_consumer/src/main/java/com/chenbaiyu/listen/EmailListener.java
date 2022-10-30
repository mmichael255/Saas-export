package com.chenbaiyu.listen;

import com.chenbaiyu.utils.MailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EmailListener implements MessageListener {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onMessage(Message message) {
        try {
            Map map = objectMapper.readValue(message.getBody(), Map.class);
            String name = (String) map.get("name");
            String deptName = (String) map.get("deptName");
            String email = (String) map.get("email");
            String title = "歡迎使用Saas-Export平台";
            String content = "親愛的"+name+",</br>感謝您注冊Saas-Export平台，您入駐的部門是<strong>"+deptName+"</strong>，讓我們共創未來";
            System.out.println(content);
            MailUtil.sendMsg(email,title,content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
