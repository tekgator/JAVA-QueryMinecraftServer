package com.tekgator.queryminecraftserver.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.tekgator.queryminecraftserver.api.QueryException;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public abstract class QueryStatusTcpBase extends QueryStatusBase {

    protected Socket socket = null;
    protected OutputStream outputStream;
    protected DataOutputStream dataOutputStream;

    protected InputStream inputStream;
    protected DataInputStream dataInputStream;

    public QueryStatusTcpBase(ServerDNS serverDNS, int timeOut) {
        super(serverDNS, timeOut);
    }


    protected void connect()
            throws QueryException {
        try {
            this.socket = new Socket();

            // connect to server
            this.socket.setSoTimeout(this.timeOut);
            this.socket.connect(this.inetSocketAddress, this.timeOut);

            // retrieve the output stream from the socket
            this.outputStream = this.socket.getOutputStream();
            this.dataOutputStream = new DataOutputStream(this.outputStream);

            // retrieve the input stream from the socket
            this.inputStream = this.socket.getInputStream();
            this.dataInputStream = new DataInputStream(this.inputStream);
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM,
                    String.format("TCP Connection to '%s:%d' failed", this.inetSocketAddress.getHostString(), this.inetSocketAddress.getPort()));
        }
    }

    protected void disconnect () {
        try {
            // close all streams and socket connection to server
            if (this.dataOutputStream != null)
                this.dataOutputStream.close();

            if (this.outputStream != null) 
                this.outputStream.close();

            if (this.dataInputStream != null)
                this.dataInputStream.close();

            if (this.inputStream != null)
                this.inputStream.close();

            if (this.socket != null)
                this.socket.close();
        } catch (IOException e) {
            // failed to disconnect?!, just keep going then
        } finally {
            this.dataOutputStream = null;
            this.outputStream = null;
            this.dataInputStream = null;
            this.inputStream = null;
            this.socket = null;
        }
    }

    protected abstract void sendHandShake() throws QueryException;
    
    protected abstract String receiveStatusResponse() throws QueryException;
    
}