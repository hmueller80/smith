/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.parser;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class ParsedObject<T> {
    Set<ParsingMessage> messages=new HashSet<>();
    T object;

    public ParsedObject(T object) {
        this.object = object;
    }
    
    public ParsedObject(T object,Set<ParsingMessage> messages) {
        this.object = object;
        this.messages= messages;
    }

    public Set<ParsingMessage> getWarnings() {
        return messages;
    }

    public T getObject() {
        return object;
    }
    
    public void addMessage(String summary, String description){
        messages.add(new ParsingMessage(summary,description));
    }

    public void addMessages(Set<ParsingMessage> messages){
        this.messages.addAll(messages);
    }
    
    
}
