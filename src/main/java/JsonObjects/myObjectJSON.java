package JsonObjects;

public class myObjectJSON {

    private String correlationId;
    private String actionId;

    public myObjectJSON(String correlationId, String actionId) {
        this.correlationId = correlationId;
        this.actionId = actionId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setCorrelationID(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setActionID(String actionId) {
        this.actionId = actionId;
    }

    public String toString(){
        return "correlationId=" + this.correlationId + ", actionId=" + this.actionId;
    }

}
