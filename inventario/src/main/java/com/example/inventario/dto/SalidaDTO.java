package com.example.inventario.dto;


public class SalidaDTO {
    private String qr;
    private Double precioVenta;

    public String getQr() {
        return qr;
    }
    public void setQr(String qr) {
        this.qr = qr;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
}
