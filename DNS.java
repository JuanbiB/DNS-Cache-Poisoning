import java.util.*;

public class DNS implements Node{
    //Instance Variables: Root name server and cache
    private NameServer root_;
    private Cache cache_;


    /**
     * Constructor for DNS server object.
     * @param root the root name server
     */
    public DNS(Name root) {
        root_ = root;
        cache_ = new Cache();
    }

    /**
     * Takes in domain name (currently), asks root_ for IP address
     * @param domain the domain name to search for
     * @return address address corresponding to the domain parameter
     */
    @Override
    public void message(Node src, Message message) {
        if (cache_.containsEntry(message)){
            src.message(lookupEntry(message));
        }
        if (message.getType() == MessageTypes.FINAL){
            // Add entry to cache
            // add TXID check
            if ()
            cache_.addEntry(message.getQuery(), message.getAnswer());
            src.message()
        }
        if (message.getType()==MessageTypes.WHERE){
            root_.message(this, message)
        }
        if (message.getType()==MessageTypes.TRY){
            NameServer nextServer = message.getNextServer();
            nextServer.message(this, new Message(message.getQuery()));
        }
    }
}