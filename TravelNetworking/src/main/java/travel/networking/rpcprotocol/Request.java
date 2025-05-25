package travel.networking.rpcprotocol;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private Object data;

    private Request(){}

    public RequestType type(){return type;}

    public Object data(){return data;}

    public static class Builder{
        private Request request = new Request();

        public Builder type(RequestType type) {
            request.type(type);
            return this;
        }

        public Builder data(Object data) {
            request.data(data);
            return this;
        }

        public Request build() {
            return request;
        }
    }

    public void data(Object data) {this.data = data;}

    private void type(RequestType type) {this.type = type;}

}
