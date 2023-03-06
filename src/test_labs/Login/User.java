package test_labs.Login;

public class User {

    public String name;
    public String password;
    public String host;
    public String port;


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {this.host = host;}

    public void setPort(String port) {this.port = port;}

    public String getName() {return name;}

    public String getPassword() {return password;}

    public String getHost() {return host;}

    public String getPort() {return port;}

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
