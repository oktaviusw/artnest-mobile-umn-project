package id.ac.umn.mobile.myapplication;

import java.io.Serializable;

public class ModelChatInformation implements Serializable {
    private String idSender;
    private String nameSender;
    private String idReceiver;
    private String nameReceiver;
    private String typeChat;
    private String contextChat;

    public ModelChatInformation(String idSender, String nameSender, String idReceiver, String nameReceiver, String typeChat, String contextChat) {
        this.idSender = idSender;
        this.nameSender = nameSender;
        this.idReceiver = idReceiver;
        this.nameReceiver = nameReceiver;
        this.typeChat = typeChat;
        this.contextChat = contextChat;
    }

    public String getIdSender() {
        return idSender;
    }

    public String getNameSender() {
        return nameSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public String getNameReceiver() {
        return nameReceiver;
    }

    public String getTypeChat() {
        return typeChat;
    }

    public String getContextChat() {
        return contextChat;
    }
}
