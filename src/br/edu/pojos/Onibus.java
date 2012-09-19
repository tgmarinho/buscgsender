package br.edu.pojos;

public class Onibus {
    private Integer id;
    private String nome;
    private Double latitude;
    private Double longitude;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public String toString()
    {
        return "Sou o onibus " + this.getNome() + " e estou em lat = " + this.getLatitude()
                + " e long = " + this.getLongitude();
    }
}
