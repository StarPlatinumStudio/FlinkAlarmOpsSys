package Source.Custom;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class SourceFromSocketClient extends RichSourceFunction<String> {
   private Socket socket;
   private int port;
   public SourceFromSocketClient(Integer port){
       this.port=port;
   }
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ServerSocket server = new ServerSocket(port);
        socket=server.accept();

    }

    @Override
    public void close() throws Exception {
        super.close();
        if(socket!=null){
            socket.close();
        }
    }
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (true){
    BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
ctx.collect(reader.readLine());
    }}

@Override
    public void cancel() {
    }
}
