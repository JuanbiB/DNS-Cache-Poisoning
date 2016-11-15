import java.util.*;
public class DNS{
    //Instance Variables: Root name server and cache
    private Name root_;
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
    public Response Answer(Request req){
        if (cache_.contains(req)){
            return cache_.get(req);
        }
        return root_.Answer(req);
    }
}