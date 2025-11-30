package com.classes.main;

import com.classes.networking.*;
import com.classes.DTO.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCombateMultiplayer extends TelaCombate {
    private NetworkManager networkManager;
    private Jogador jogadorLocal;
    private Jogador jogadorRemoto;
    private boolean minhaVez;
    
    public TelaCombateMultiplayer(TelaAventura pai, Jogador jogadorLocal, 
                                 Jogador jogadorRemoto, Inimigo inimigo, 
                                 NetworkManager networkManager) {
        super(pai, jogadorLocal, inimigo);
        this.jogadorLocal = jogadorLocal;
        this.jogadorRemoto = jogadorRemoto;
        this.networkManager = networkManager;
        this.minhaVez = networkManager.isHost(); // Host começa
        
        adaptarParaMultijogador();
    }
    
    private void adaptarParaMultijogador() {
        // Modificar interface para mostrar ambos os jogadores
        setTitle("Combate Cooperativo - " + getInimigo().getNome() + " - VS " + jogadorRemoto.getNome());
        
        // ✅ ADAPTAR A INTERFACE PARA MOSTRAR 2 JOGADORES
        adaptarInterfaceParaDoisJogadores();
        
        // Sincronizar com o outro jogador
        if (minhaVez) {
            habilitarBotoes();
            adicionarLog("🎯 Sua vez! Faça sua ação.");
        } else {
            desabilitarBotoes();
            adicionarLog("⏳ Aguardando " + jogadorRemoto.getNome() + "...");
        }
        
        adicionarLog("🤝 Modo Cooperativo: " + jogadorLocal.getNome() + " e " + jogadorRemoto.getNome());
    }
    
    private void adaptarInterfaceParaDoisJogadores() {
        // ✅ ADICIONAR INFORMAÇÕES DO JOGADOR REMOTO NA INTERFACE
        // Você precisará modificar o layout para mostrar ambos os jogadores
        
        // Exemplo simplificado - adicionar info do jogador remoto no log
        adicionarLog("🎮 Jogador Remoto: " + jogadorRemoto.getNome() + 
                    " (HP: " + jogadorRemoto.getHp() + "/" + jogadorRemoto.getHpMax() + ")");
    }
    
    @Override
    protected void ataqueNormal() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        super.ataqueNormal();
        
        // ✅ ENVIAR AÇÃO PARA O OUTRO JOGADOR
        CombatActionData actionData = new CombatActionData(
            "ATAQUE_NORMAL", 
            getInimigo().getHp(),
            getJogador().getHp()
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        passarVez();
    }
    
    @Override
    protected void usarHabilidade() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        super.usarHabilidade();
        
        // ✅ ENVIAR AÇÃO PARA O OUTRO JOGADOR
        CombatActionData actionData = new CombatActionData(
            "HABILIDADE", 
            getInimigo().getHp(),
            getJogador().getHp()
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        passarVez();
    }
    
    @Override
    protected void usarItem() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        super.usarItem();
        
        // ✅ ENVIAR AÇÃO PARA O OUTRO JOGADOR
        CombatActionData actionData = new CombatActionData(
            "USAR_ITEM", 
            getInimigo().getHp(),
            getJogador().getHp()
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        passarVez();
    }
    
    @Override
    protected void defender() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        super.defender();
        
        // ✅ ENVIAR AÇÃO PARA O OUTRO JOGADOR
        CombatActionData actionData = new CombatActionData(
            "DEFENDER", 
            getInimigo().getHp(),
            getJogador().getHp()
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        passarVez();
    }
    
    @Override
    protected void turnoInimigo() {
        // ✅ SINCRONIZAR TURNO DO INIMIGO ENTRE AMBOS OS JOGADORES
        super.turnoInimigo();
        
        // Enviar resultado do turno
        CombatActionData turnData = new CombatActionData(
            "INIMIGO_ATACOU", 
            getInimigo().getHp(),
            getJogador().getHp()
        );
        
        GameMessage turnMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            turnData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(turnMsg);
    }
    
    @Override
    protected void vitoria() {
        // ✅ SINCRONIZAR VITÓRIA
        adicionarLog("🎉 VITÓRIA COOPERATIVA!");
        adicionarLog("🏆 " + jogadorLocal.getNome() + " e " + jogadorRemoto.getNome() + " venceram juntos!");
        
        GameMessage victoryMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_END,
            "VITORIA",
            jogadorLocal.getId()
        );
        networkManager.sendMessage(victoryMsg);
        
        super.vitoria();
    }
    
    @Override
    protected void derrota() {
        // ✅ SINCRONIZAR DERROTA
        adicionarLog("💀 DERROTA COOPERATIVA...");
        
        GameMessage defeatMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_END,
            "DERROTA",
            jogadorLocal.getId()
        );
        networkManager.sendMessage(defeatMsg);
        
        super.derrota();
    }
    
    private void passarVez() {
        minhaVez = false;
        desabilitarBotoes();
        adicionarLog("↪️ Vez passada para " + jogadorRemoto.getNome());
        
        // ✅ HABILITAR BOTÕES DO OUTRO JOGADOR (via mensagem de rede)
        GameMessage vezMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            "SUA_VEZ",
            jogadorLocal.getId()
        );
        networkManager.sendMessage(vezMsg);
    }
    
    /**
     * ✅ PROCESSAR MENSAGENS DE COMBATE DO OUTRO JOGADOR
     */
    public void processarMensagemCombate(GameMessage message) {
        switch (message.getType()) {
            case COMBAT_ACTION:
                Object data = message.getData();
                if (data instanceof CombatActionData) {
                    CombatActionData actionData = (CombatActionData) data;
                    processarAcaoCombate(actionData, message.getPlayerId());
                } else if (data instanceof String) {
                    String acao = (String) data;
                    if ("SUA_VEZ".equals(acao)) {
                        minhaVez = true;
                        habilitarBotoes();
                        adicionarLog("🎯 Sua vez! Faça sua ação.");
                    }
                }
                break;
                
            case COMBAT_END:
                String resultado = (String) message.getData();
                if ("VITORIA".equals(resultado)) {
                    adicionarLog("🎉 Jogador remoto venceu o combate!");
                } else if ("DERROTA".equals(resultado)) {
                    adicionarLog("💀 Jogador remoto foi derrotado...");
                }
                break;
                
            case PLAYER_STATS:
                // ✅ ATUALIZAR STATS DO JOGADOR REMOTO
                if (message.getData() instanceof Jogador) {
                    Jogador jogadorAtualizado = (Jogador) message.getData();
                    atualizarJogadorRemoto(jogadorAtualizado);
                }
                break;
        }
    }
    
    private void processarAcaoCombate(CombatActionData actionData, int playerId) {
        // ✅ SINCRONIZAR ESTADO DO COMBATE
        getInimigo().setHp(actionData.getInimigoHp());
        //getJogador().setHp(actionData.getJogadorHp()); // Não atualizar HP local do outro jogador
        
        adicionarLog("📊 Sincronizado: Inimigo HP = " + actionData.getInimigoHp());
        atualizarStatus();
    }
    
    private void atualizarJogadorRemoto(Jogador jogadorAtualizado) {
        // ✅ ATUALIZAR INFORMAÇÕES DO JOGADOR REMOTO
        jogadorRemoto.setHp(jogadorAtualizado.getHp());
        jogadorRemoto.setMana(jogadorAtualizado.getMana());
        // ... atualizar outros atributos se necessário
        
        adicionarLog("📊 " + jogadorRemoto.getNome() + " atualizado: HP=" + jogadorRemoto.getHp());
    }
    
    // ✅ GETTER PARA ACESSO EXTERNO
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}

// ✅ CLASE AUXILIAR PARA DADOS DE AÇÃO DE COMBATE
class CombatActionData implements java.io.Serializable {
    private String acao;
    private int inimigoHp;
    private int jogadorHp;
    
    public CombatActionData(String acao, int inimigoHp, int jogadorHp) {
        this.acao = acao;
        this.inimigoHp = inimigoHp;
        this.jogadorHp = jogadorHp;
    }
    
    // Getters
    public String getAcao() { return acao; }
    public int getInimigoHp() { return inimigoHp; }
    public int getJogadorHp() { return jogadorHp; }
}