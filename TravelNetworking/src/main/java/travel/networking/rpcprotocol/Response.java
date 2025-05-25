package travel.networking.rpcprotocol;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType responseType;
    private Object data;

    private Response(){}

    public ResponseType type(){ return responseType; }

    public Object data(){ return data; }

    private void type(ResponseType type){ responseType = type; }

    private void data(Object data){ this.data = data; }

    public static class Builder{
        private Response response = new Response();

        public Builder type(ResponseType type){
            response.type(type);
            return this;
        }

        public Builder data(Object data){
            response.data(data);
            return this;
        }

        public Response build(){
            return response;
        }
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseType=" + responseType +
                ", data=" + data +
                '}';
    }
}
