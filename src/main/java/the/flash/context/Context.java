package the.flash.context;

import the.flash.protocol.Packet;

// 使用策略模式取代if..else
public class Context {

    private Packet packet;

    public Context(Packet packet){
        this.packet = packet;
    }

    public void contextInterface(){
        // todo
    }

}
