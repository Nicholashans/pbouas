/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos.application;

/**
 *
 * @author chris
 */
public class debitCards {
    
    static String cardNumber = "1234567890";
    static float balance = 1000000.0f;
    
    public static float getBalance(){
        return balance;
    }
    
    public static String getCardNumber(){
        return cardNumber;
    }
    
    public static float updateBalance(float updatedBalance){
        return balance = updatedBalance;
    }
    
}
