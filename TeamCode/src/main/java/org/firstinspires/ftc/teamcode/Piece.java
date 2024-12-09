// All 27 Piece classifications for each piece on the cube.  Will be based on the above cube map.
    // Identification string goes in order of red-orange side, blue-green side, and white-yellow side.
    public enum Piece{
        R0G2W0 {@Override public String toString() { return "R0G2W0"; }},
        G1W1 {@Override public String toString() { return "G1W1"; }},
        O2G0W2 {@Override public String toString() { return "O2G0W2"; }},
        R1W3 {@Override public String toString() { return "R1W3"; }},
        W4 {@Override public String toString() { return "W4"; }}, // center
        O1W5 {@Override public String toString() { return "O1W5"; }},
        R2B0W6 {@Override public String toString() { return "R2B0W6"; }},
        B1W7 {@Override public String toString() { return "B1W7"; }},
        O0B2W8 {@Override public String toString() { return "O0B2W8"; }},
        R3G5 {@Override public String toString() { return "R3G5"; }},
        G4 {@Override public String toString() { return "G4"; }}, // center
        O5G3 {@Override public String toString() { return "O5G3"; }},
        R4 {@Override public String toString() { return "R4"; }}, // center
        NULL {@Override public String toString() { return "NULL"; }}, // the core... dk why i put an enum for this
        O4 {@Override public String toString() { return "O4"; }}, // center
        R5B3 {@Override public String toString() { return "R5B3"; }},
        B4 {@Override public String toString() { return "B4"; }}, // center
        O3B5 {@Override public String toString() { return "O3B5"; }},
        R6G8Y6 {@Override public String toString() { return "R6G8Y6"; }},
        G7Y7 {@Override public String toString() { return "G7Y7"; }},
        O8G6Y8 {@Override public String toString() { return "O8G6Y8"; }},
        R7Y3 {@Override public String toString() { return "R7Y3"; }},
        Y4 {@Override public String toString() { return "Y4"; }}, // center
        O7Y5 {@Override public String toString() { return "O7Y5"; }},
        R8B6Y0 {@Override public String toString() { return "R8B6Y0"; }},
        B7Y1 {@Override public String toString() { return "B7Y1"; }},
        O6B8Y2 {@Override public String toString() { return "O6B8Y2"; }};

        public static Color getAlpha(Piece p){
            switch(p){
                case R0G2W0: return Color.RED;
                case G1W1: return null;
                case O2G0W2: return Color.ORANGE;
                case R1W3: return Color.RED;
                case W4: return null;
                case O1W5: return Color.ORANGE;
                case R2B0W6: return Color.RED;
                case B1W7: return null;
                case O0B2W8: return Color.ORANGE;
                case R3G5: return Color.RED;
                case G4: return null; 
                case O5G3: return Color.ORANGE;
                case R4: return Color.RED;
                case NULL: return null;
                case O4: return Color.ORANGE;
                case R5B3: return Color.RED;
                case B4: return null; 
                case O3B5: return Color.ORANGE;
                case R6G8Y6: return Color.RED;
                case G7Y7: return null;
                case O8G6Y8: return Color.ORANGE;
                case R7Y3: return Color.RED;
                case Y4: return null; 
                case O7Y5: return Color.ORANGE;
                case R8B6Y0: return Color.RED;
                case B7Y1: return null;
                case O6B8Y2: return Color.ORANGE;
                default: return null;
            }
        }
    
        // return the color on the blue-green side of this piece
        public static Color getBeta(Piece p){
            switch(p){
                case R0G2W0: return Color.GREEN;
                case G1W1: return Color.GREEN;
                case O2G0W2: return Color.GREEN;
                case R1W3: return null;
                case W4: return null;
                case O1W5: return null;
                case R2B0W6: return Color.BLUE;
                case B1W7: return Color.BLUE;
                case O0B2W8: return Color.BLUE;
                case R3G5: return Color.GREEN;
                case G4: return Color.GREEN; 
                case O5G3: return Color.GREEN;
                case R4: return null;
                case NULL: return null; 
                case O4: return null;
                case R5B3: return Color.BLUE;
                case B4: return Color.BLUE; 
                case O3B5: return Color.BLUE;
                case R6G8Y6: return Color.GREEN;
                case G7Y7: return Color.GREEN;
                case O8G6Y8: return Color.GREEN;
                case R7Y3: return null;
                case Y4: return null; 
                case O7Y5: return null;
                case R8B6Y0: return Color.BLUE;
                case B7Y1: return Color.BLUE;
                case O6B8Y2: return Color.BLUE;
                default: return null;
            }
        }
    
        // return the color on the white-yellow side of this piece
        public static Color getGamma(Piece p){
            switch(p){
                case R0G2W0: return Color.WHITE;
                case G1W1: return Color.WHITE;
                case O2G0W2: return Color.WHITE;
                case R1W3: return Color.WHITE;
                case W4: return Color.WHITE;
                case O1W5: return Color.WHITE;
                case R2B0W6: return Color.WHITE;
                case B1W7: return Color.WHITE;
                case O0B2W8: return Color.WHITE;
                case R3G5: return null;
                case G4: return null;
                case O5G3: return null;
                case R4: return null;
                case NULL: return null;
                case O4: return null;
                case R5B3: return null;
                case B4: return null;
                case O3B5: return null;
                case R6G8Y6: return Color.YELLOW;
                case G7Y7: return Color.YELLOW;
                case O8G6Y8: return Color.YELLOW;
                case R7Y3: return Color.YELLOW;
                case Y4: return Color.YELLOW; 
                case O7Y5: return Color.YELLOW;
                case R8B6Y0: return Color.YELLOW;
                case B7Y1: return Color.YELLOW;
                case O6B8Y2: return Color.YELLOW;
                default: return null;
            }
        }
    }