package com.tekgator.queryminecraftserver.api;

import com.tekgator.queryminecraftserver.internal.QueryStatusTcp;
import com.tekgator.queryminecraftserver.internal.QueryStatusTcpDepreciated;
import com.tekgator.queryminecraftserver.internal.QueryStatusUdp;
import com.tekgator.queryminecraftserver.internal.ServerDNS;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public final class QueryStatus {

    private final ServerDNS serverDNS;
    private final Protocol protocol;
    private final int timeOut;

    private Status status;

    private QueryStatus(Builder builder) 
            throws QueryException {
        this.serverDNS = new ServerDNS(builder.hostName, builder.port);
        this.timeOut = builder.timeOut;
        this.protocol = builder.protocol;
    }

    /**
     * Return last queried status of the Minecraft server without invoking a refresh
     * (at least if loaded already once)
     * 
     * @return Status of the Minecraft Server
     * @throws QueryException
     */
    public Status getStatus() 
            throws QueryException {
        
        if (this.status != null)
            return this.status;

        return refreshStatus();
    }

    /**
     * Queried status of the Minecraft server
     * 
     * @return Status of the Minecraft Server
     * @throws QueryException
     */
    public Status refreshStatus() 
            throws QueryException {
        
        switch(this.protocol) {
            case TCP_DEPRECIATED:
                this.status = new QueryStatusTcpDepreciated(this.serverDNS, this.timeOut).getStatus();
                break;
            case TCP:
                this.status = new QueryStatusTcp(this.serverDNS, this.timeOut).getStatus();
                break;
            case UDP_BASIC:
            case UDP_FULL:
                this.status = new QueryStatusUdp(this.protocol, this.serverDNS, this.timeOut).getStatus();
                break;
            default:
                break;
        }
        return this.status;
    }


    /**
     * Builder class to get an instance of StatusQuery 
     *
     * @author Patrick Weiss <info@tekgator.com>
     * @see QueryStatus
     */
    public static final class Builder {
        private final String hostName;
        private int port = 0;
        private int timeOut = 1000;
        private Protocol protocol = Protocol.TCP;
       
        /**
         * Constructor of the builder class
         * 
         * @param hostname or ip address of the Minecraft Server
         * 
         */
        public Builder(String hostName) {
            this.hostName = hostName;
        }

        /**
         * Set port of the minecraft server for the query. If not ommited it will be loaded via SRV 
         * record and in case no SRV revord is found the default Minecraft port (=25565) is used 
         * 
         * @param port of the Minecraft Server
         * 
         */
        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * @param The used Minecraft Protocol version for invoking the query
         * @see Protocol
         */
        public Builder setProtocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        /**
         * Set socket timeout
         * @param timeout in MS
         */
        public Builder setTimeout(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @return New instance of the QueryStatus class
         */
        public QueryStatus build() throws QueryException {
            return new QueryStatus(this);
        }     
    }

}