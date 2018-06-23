package com.polchlopek.praca.magisterska.controller;

import com.polchlopek.praca.magisterska.config.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.logging.Level;
import java.util.logging.Logger;

import static jssc.SerialPort.MASK_RXCHAR;


public class STMCommunication {

    @FXML
    private Label labelValue;

    @FXML
    private ComboBox comboBoxPorts;

    @FXML
    private ToggleButton diode;

    private ObservableList<String> portList;

    private SerialPort stmPort = null;




    @FXML
    public void disconnect(){
        disconnectSTM();
    }

    @FXML
    public void searchPort(){

        detectPort();
        comboBoxPorts.setItems(portList);

        comboBoxPorts.valueProperty()
                .addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {

                        System.out.println(newValue);
                        disconnectSTM();
                        connectSTM(newValue);
                    }

                });
    }

    @FXML
    private void toggleDiode(){
        try {
            if(diode.isSelected()){
                if(stmPort != null){
//                    stmPort.writeByte((byte)0x01);
//                    stmPort.writeString("Test Test TestTest");
//                    stmPort.writeIntArray(new int[]{0xFF, 0x00, 0xFF});

                    System.out.println("LED 13 ON");
                }else{
                    System.out.println("stm not connected!");
                }
            }else {
                if(stmPort != null){
                    stmPort.writeByte((byte)0x00);
                    System.out.println("LED 13 OFF");
                }else{
                    System.out.println("stm not connected!");
                }
            }
        }catch (SerialPortException ex) {
            Logger.getLogger(Main.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }


    private boolean connectSTM(String port){

        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);


            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try {

                        byte[] b = serialPort.readBytes();
                        String message = "";

                        System.out.println("*********************************************");
                        System.out.println("B: " + b[0] + ", size: " + b.length );
                        for (byte c: b){
                            System.out.print((char)c);
                            message += (char)c;
                        }

                        int value = b[0] & 0xff;    //convert to int
                        String st = String.valueOf(message);
                        System.out.println("ST: " + st);
                        System.out.println("*********************************************");

                        //Update label in ui thread
                        Platform.runLater(() -> {
                            labelValue.setText(st);
                        });

                    } catch (SerialPortException ex) {
                        Logger.getLogger(Main.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }

                }
            });

            stmPort = serialPort;
            success = true;
        } catch (SerialPortException ex) {
            Logger.getLogger(Main.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex.toString());
        }

        return success;
    }

    private void disconnectSTM(){

        if(stmPort != null){
            try {
                stmPort.removeEventListener();

                if(stmPort.isOpened()){
                    stmPort.closePort();
                }

            } catch (SerialPortException ex) {
                Logger.getLogger(Main.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void detectPort(){

        portList = FXCollections.observableArrayList();
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            portList.add(name);
        }

    }


}