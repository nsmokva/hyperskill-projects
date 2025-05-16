package tetris;

public enum PositionStatus {
    TAKEN("0"), FREE("-");
    private final String sign;
    PositionStatus(String sign){
    this.sign = sign;
    }

    public String getSign(){
        return sign;
    }
}
