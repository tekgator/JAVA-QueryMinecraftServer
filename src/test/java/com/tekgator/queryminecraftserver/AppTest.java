package com.tekgator.queryminecraftserver;

import static org.junit.Assert.assertTrue;

import com.tekgator.queryminecraftserver.api.QueryStatus;

import org.junit.Ignore;
import org.junit.Test;

public class AppTest {

    final int mMultiCnt = 3;

    @Test
    public void TcpQuery () {
        try {
            System.out.println("-------------------------------------------------------------");
            System.out.println(new QueryStatus.Builder("play.lemoncloud.net")
                                        .setProtocolTcp()
                                        .build()
                                        .getStatus()
                                        .toJson());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    }

    @Test
    public void TcpQueryMulti () {
        QueryStatus queryStatus;
        
        try {
            queryStatus = new QueryStatus.Builder("play.lemoncloud.net")
                                .setProtocolTcp()
                                .build();
            
            for (int i = 1; i <= mMultiCnt; i++) {
                System.out.println(Integer.toString(i) + ".)-------------------------------------------------------------");
                System.out.println(queryStatus.refreshStatus().toJson());
                if (i != mMultiCnt)
                    Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    }    

    @Test
    //@Ignore // If no more Minecraft Servers <= 1.6 are available this test has to be deactivated
    public void TcpQueryDepreciated () {
        try {
            System.out.println("-------------------------------------------------------------");
            System.out.println(new QueryStatus.Builder("69.175.124.74")
                                        .setProtocolTcpDepreciated() // Currently MegaCraft (69.175.124.74) still on 1.5.1
                                        .build()
                                        .refreshStatus()
                                        .toJson());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    }

    @Test
    //@Ignore // If no more Minecraft Servers <= 1.6 are available this test has to be deactivated
    public void TcpQueryDepreciatedMulti () {
        QueryStatus queryStatus;
        
        try {
            queryStatus = new QueryStatus.Builder("69.175.124.74") 
                                .setProtocolTcpDepreciated() // Currently MegaCraft (69.175.124.74) still on 1.5.1
                                .build();

            for (int i = 1; i <= mMultiCnt; i++) {
                System.out.println(Integer.toString(i) + ".)-------------------------------------------------------------");
                System.out.println(queryStatus.refreshStatus().toJson());
                if (i != mMultiCnt)
                    Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    } 

    @Test
    @Ignore // No more public UPD activated Minecraft Servers found, therefore the test has been deactivated for right now
    public void UdpQueryBasic () {
        try {
            System.out.println("-------------------------------------------------------------");
            System.out.println(new QueryStatus.Builder("play.lemoncloud.net")
                                        .setProtocolUdpBasic()
                                        .build()
                                        .refreshStatus()
                                        .toJson());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    }   
    
    @Test
    @Ignore // No more public UPD activated Minecraft Servers found, therefore the test has been deactivated for right now
    public void UdpQueryBasicMulti () {
        QueryStatus queryStatus;
        
        try {
            queryStatus = new QueryStatus.Builder("play.lemoncloud.net")
                                .setProtocolUdpBasic()
                                .build();

            for (int i = 1; i <= mMultiCnt; i++) {
                System.out.println(Integer.toString(i) + ".)-------------------------------------------------------------");
                System.out.println(queryStatus.refreshStatus().toJson());
                if (i != mMultiCnt)
                    Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    } 

    @Test
    @Ignore // No more public UPD activated Minecraft Servers found, therefore the test has been deactivated for right now
    public void UdpQueryFull () {
        try {
            System.out.println("-------------------------------------------------------------");
            System.out.println(new QueryStatus.Builder("play.lemoncloud.net")
                                        .setProtocolUdpFull()
                                        .build()
                                        .refreshStatus()
                                        .toJson());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    }   
    
    @Test
    @Ignore // No more public UPD activated Minecraft Servers found, therefore the test has been deactivated for right now
    public void UdpQueryFullMulti () {
        QueryStatus queryStatus;
        
        try {
            queryStatus = new QueryStatus.Builder("play.lemoncloud.net")
                                .setProtocolUdpFull()
                                .build();
            for (int i = 1; i <= mMultiCnt; i++) {
                System.out.println(Integer.toString(i) + ".)-------------------------------------------------------------");
                System.out.println(queryStatus.refreshStatus().toJson());
                if (i != mMultiCnt)
                    Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
            return;
        }

        assertTrue( true );
    } 

}
