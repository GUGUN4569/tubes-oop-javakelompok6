package com.kelompok.resep.model;

/**
 * Custom Exception untuk menangani input yang kosong.
 * Syarat Tugas: Handle exception input kosong.
 */
public class InputKosongException extends Exception {
    public InputKosongException(String pesan) {
        super(pesan);
    }
}