package edu.java.repository;

public interface ChatLinkRepository {

    void create(long chatId, int linkId);

    void remove(long chatId, int linkId);
}
