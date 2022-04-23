public class ReadyQueueElement {
    String tag;
    int latency;
    boolean mustWait;
    ReadyQueueElement(String tag , int latency , boolean mustWait){
        this.tag=tag;
        this.latency=latency;
        this.mustWait=mustWait;
    }
}
