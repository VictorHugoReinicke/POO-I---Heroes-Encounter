package com.classes.networking;

import java.io.Serializable;
import com.classes.DTO.*;

public class GameMessage implements Serializable {
    public enum MessageType {
        PLAYER_JOIN,        // Jogador entrou
        PLAYER_ACTION,      // Ação do jogador
        COMBAT_START,       // Início de combate
        COMBAT_ACTION,      // Ação no combate
        COMBAT_END,         // Fim de combate
        PLAYER_STATS,       // Atualização de stats
        SHOP_PURCHASE,      // Compra na loja
        GAME_SYNC,          // Sincronização geral
        CHAT_MESSAGE        // Mensagem de chat
    }
    
    private MessageType type;
    private Object data;
    private int playerId;
    private long timestamp;
    
    public GameMessage(MessageType type, Object data, int playerId) {
        this.type = type;
        this.data = data;
        this.playerId = playerId;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters e Setters
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}