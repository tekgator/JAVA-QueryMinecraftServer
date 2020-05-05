package com.tekgator.queryminecraftserver.internal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.QueryException;
import com.tekgator.queryminecraftserver.api.Status;

/**
 * @author Patrick Weiss <info@tekgator.com>
 * @see http://wiki.vg/Server_List_Ping#Ping_Process
 */
public class QueryStatusTcp extends QueryStatusTcpBase {

    public QueryStatusTcp(ServerDNS serverDNS, int timeOut) {
        super(serverDNS, timeOut);
    }

    @Override
    public Status getStatus() 
            throws QueryException {
       
        try {
            // connect to server
            connect();


            // send handshake
            sendHandShake();

            // status request
            sendStatusRequest();

            // receive status response from the server
            String response = receiveStatusResponse();

            try {
                // send ping request to server
                long pingStart = System.currentTimeMillis();
                sendPingRequest(pingStart);

                // receive ping response from server
                long pingStartReceived = receivePingResponse();
                long pingEnd = System.currentTimeMillis();

                if (pingStart == pingStartReceived) {
                    this.pingStart = pingStart;
                    this.pingEnd = pingEnd;
                } else {
                    //Time received is different from time sent continue
                    // anyways as this is only for calculating the ping
                }
            } catch (QueryException e) {
                // nothing to do, use the already set time during handshake handling
            }
            
            // finally build the status object
            this.status = new StatusBuilder()
                                .setProtocol(Protocol.TCP)
                                .setLatency(calculateLatency())
                                .setServerDNS(this.serverDNS)
                                .setData(response)
                                .build();
        } finally {
            // disconnect from server
            disconnect();
        }

        return this.status;
    }

    @Override
    protected void sendHandShake()
            throws QueryException {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(b);

        try {
            handshake.writeByte(0x00);
            writeVarInt(handshake, Protocol.TCP.getValue()); //protocol version
            writeVarInt(handshake, this.serverDNS.getTargetHostName().length()); // hostname length
            handshake.writeBytes(this.serverDNS.getTargetHostName()); //hostname
            handshake.writeShort(this.serverDNS.getPort()); //port
            writeVarInt(handshake, 1); //state (1 for handshake)
            writeVarInt(dataOutputStream, b.size()); //prepend size

            this.pingStart = System.currentTimeMillis();

            dataOutputStream.write(b.toByteArray()); //write handshake packet
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.SEND_FAILED, "Failed to write handshake to socket");
        }
    }

    private void sendStatusRequest()
            throws QueryException {
        try {
            this.dataOutputStream.writeByte(0x01); //prepend size
            this.dataOutputStream.writeByte(0x00); //packet id for status request
            this.dataOutputStream.flush();
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.SEND_FAILED, "Failed to write status request to socket");
        }
    }

    @Override
    protected String receiveStatusResponse() 
            throws QueryException {

        readVarInt(dataInputStream); //read the size of packet received from the server
        int id = readVarInt(dataInputStream); //read the packet id

        if (id == -1) {
            throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, "Premature end of stream");
        } else if (id != 0x00) {
            //we want a status response
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, String.format("Invalid packetID: %d", id));
        }

        // set time when the first message is received for latency calculation
        this.pingEnd = System.currentTimeMillis();

        int jsonLength = readVarInt(dataInputStream); //length of json string

        if (jsonLength == -1) {
            throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, "Premature end of stream");
        }

        if (jsonLength == 0) {
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, "Invalid length of JSON response");
        }

        byte[] byteStream = new byte[jsonLength];
        try {
            dataInputStream.readFully(byteStream);  //read json string
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE,
                    "Server returned invalid response!");
        }
        
        return new String(byteStream);
    }

    private void sendPingRequest(long time)
            throws QueryException {
        try {
            this.dataOutputStream.writeByte(0x09); //size of packet
            this.dataOutputStream.writeByte(0x01); //0x01 for ping
            this.dataOutputStream.writeLong(time); //current time
            this.dataOutputStream.flush();
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.SEND_FAILED, "Failed to write ping request to socket");
        }
    }

    private long receivePingResponse()
            throws QueryException {

        long timeSent;

        readVarInt(dataInputStream);
        int id = readVarInt(dataInputStream);
        if (id == -1) {
            throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, "Premature end of stream");
        } else if (id != 0x01) {
            //we want a ping response
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, String.format("Invalid packetID: %d", id));
        }

        try {
            timeSent = dataInputStream.readLong();
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE);
        }

        return timeSent;
    }


}