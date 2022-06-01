import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String date;
    private String type;
    private String content;
    private String amount;
    public Account(String id, String date, String type, String content, String amount) {
        super();
        this.id = id;
        this.date = date;
        this.type = type;
        this.content = content;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
