/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.domain;

import com.google.gson.Gson;

/**
 *
 * @author Pedro Arthur
 */
public interface JsonObject<T> {

    T fromJson(String json);

    default String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, this.getClass());
    }
}
