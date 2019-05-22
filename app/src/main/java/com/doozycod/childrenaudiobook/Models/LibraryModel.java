package com.doozycod.childrenaudiobook.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibraryModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    public List<LibraryDetails> getLibrary_list_data() {
        return library_list_data;
    }

    public void setLibrary_list_data(List<LibraryDetails> library_list_data) {
        this.library_list_data = library_list_data;
    }

    @SerializedName("library_list_data")
    @Expose
    private List<LibraryDetails> library_list_data = null;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class LibraryDetails {
        @SerializedName("library_id")
        @Expose
        private String library_id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("audio_message")
        @Expose
        private String audio_message;
        @SerializedName("audio_story")
        @Expose
        private String audio_story;

        public String getLibrary_id() {
            return library_id;
        }

        public void setLibrary_id(String library_id) {
            this.library_id = library_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAudio_message() {
            return audio_message;
        }

        public void setAudio_message(String audio_message) {
            this.audio_message = audio_message;
        }

        public String getAudio_story() {
            return audio_story;
        }

        public void setAudio_story(String audio_story) {
            this.audio_story = audio_story;
        }


    }


}
