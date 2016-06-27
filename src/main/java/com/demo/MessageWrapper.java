/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import com.models.common.Recipient;
import com.models.send.Message;

 public  class MessageWrapper {
        private  Recipient recipient;
        private  Message message;

        public Recipient getRecipient() {
            return recipient;
        }

        public Message getMessage() {
            return message;
        }

        public void setRecipient(Recipient recipient) {
            this.recipient = recipient;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
        
        public MessageWrapper(){}
        public MessageWrapper(String recipientId, Message message) {
            this.recipient = new Recipient(recipientId);
            this.message = message;
        }
    }