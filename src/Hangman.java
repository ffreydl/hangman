import java.util.Scanner;

public class Hangman {
    private static int difficulty = 0; // 0 = easy, 1 = medium; 2 = difficult
    private String wordToGuess;
    private char[] currentGuess; // letters that haven't been guessed get displayed as "_"
    private int attemptsLeft = 7; // because of the Hangman visual this is only allowed to be from 1 to 7
    private char[] previousGuess;
    private int guessCount = 0;
    private int correctGuess = 0;
    private int wrongGuess = 0;

    private static String[] hangmanStages = {

            "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
    };

    public Hangman(String wordToGuess){ // constructor
        this.wordToGuess = wordToGuess.toLowerCase();
        this.currentGuess = new char[wordToGuess.length()];
        this.previousGuess = new char[52]; // (52 is "hardcode"a solution)
        initializeCurrentGuess();
    }

    public static void main(String[] args) {
        Hangman game = new Hangman(getWord(difficulty).toLowerCase());
        game.start();

    }

    private void initializeCurrentGuess() {
        for (int i = 0; i < wordToGuess.length(); i++) {
            currentGuess[i] = '_';
        }
    }

    private void displayGame() {
        int index = hangmanStages.length - attemptsLeft;
        if(index > 6) {
            index = 6;
        }
        System.out.println(hangmanStages[index]);
        System.out.println();
        for (char c : currentGuess) {
            System.out.print(c + " ");
        }
        System.out.println();
        System.out.println();
        System.out.printf("Turn: \t");
        for(int i = 0; i < guessCount; i++) {
            System.out.printf("%d ", i + 1);
        }
        System.out.println();
        System.out.printf("Guess: \t");
        for(int i = 0; i < guessCount; i++) {
            if(isCorrectGuess(previousGuess[i])){
                System.out.printf("\u001B[32m%c \u001B[0m", previousGuess[i]);  // Green for correct guess
            } else {
                System.out.printf("\u001B[31m%c \u001B[0m", previousGuess[i]);  // Red for incorrect guess
            }
        }
        System.out.println();
        System.out.println();
    }

    private void userInput() {
        char guess = 0;
        while (guess == 0) {
            System.out.println("Choose a letter: ");
            String input = new Scanner(System.in).next();
            if (input.length() == 1 && input.matches("[a-zA-Z]")) { // range a-z or A-Z
                guess = Character.toLowerCase(input.charAt(0));
                if(isPreviousGuess(guess)){
                    System.out.println("You already guessed that letter. Try again.");
                    guess = 0; // reset guess
                }
            }
        }
        guess(guess);
    }

    public void start(){
        System.out.println("Debug> Word: " + wordToGuess);
        while (!isGameLost() && !isGameWon()) {
            displayGame();
            userInput();
        }
    }

    public void guess(char guess) { // checks if guessed letter is in the word
        boolean isCorrect = false;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess) {
                currentGuess[i] = guess;
                isCorrect = true;
            }
        }
        previousGuess[guessCount] = guess;
        guessCount++;
        if (isCorrect) {
            correctGuess++;
        } else {
            attemptsLeft--;
            wrongGuess++;
        }
    }

    public boolean isGameWon() { // does currentGuess include "_" ? if not game is won
        for (char c : currentGuess) {
            if (c == '_') {
                return false;
            }
        }
        displayGame();
        System.out.printf("You guessed the word, \u001B[32m%s\u001B[0m!", wordToGuess);
        System.out.println();
        System.out.println();
        System.out.println("Your grade: " + calculateGrade());
        return true;
    }

    public boolean isGameLost() {
        if (attemptsLeft == 0) {
            displayGame();
            System.out.printf("The word was: \u001B[32m%s\u001B[0m, you guessed \u001B[31m%d\u001B[0m out of \u001B[32m%d\u001B[0m characters.", wordToGuess, correctGuess, wordToGuess.length());
            System.out.println();
            System.out.println();
            System.out.println("Your grade: " + calculateGrade());
            return true;
        }
        return false;
    }

    private boolean isCorrectGuess(char guess) {
        for (char c : wordToGuess.toCharArray()) {
            if (c == guess) {
                return true;
            }
        }
        return false;
    }

    private boolean isPreviousGuess(char guess) {
        for (int i = 0; i < guessCount; i++) {
            if (previousGuess[i] == guess) {
                return true;
            }
        }
        return false;
    }

    private String calculateGrade() {
        String grade = "invalid";
        int totalGuess;

        totalGuess = correctGuess + wrongGuess;

        double correctPercentage = (double) correctGuess / totalGuess * 100;

        if (correctPercentage >= 90) {
            grade = "\u001B[32mA (90-100%)\u001B[0m";  // Green
        } else if (correctPercentage >= 80) {
            grade = "\u001B[36mB (80-89%)\u001B[0m";    // Cyan
        } else if (correctPercentage >= 70) {
            grade = "\u001B[33mC (70-79%)\u001B[0m";    // Yellow
        } else if (correctPercentage >= 60) {
            grade = "\u001B[31mD (60-69%)\u001B[0m";    // Red
        } else {
            grade = "\u001B[31mF (Below 60%)\u001B[0m"; // Red
        }

        return grade;
    }


    private static String getWord(int difficulty) { // words generated with ChatGPT
        String[] easyWords = {"apple", "ball", "cat", "dog", "elephant", "frog", "goat", "hat", "ice", "jam", "kite", "lemon", "mouse", "nose", "owl", "pen", "queen", "rain", "snake", "tree", "umbrella", "vase", "wall", "xylophone", "yellow", "zebra", "ant", "bear", "cake", "duck", "egg", "fish", "grass", "horse", "igloo", "jelly", "king", "lion", "moon", "night", "orange", "pig", "quilt", "rabbit", "star", "tiger", "unicorn", "violin", "wolf", "xmas", "year", "zero", "art", "beach", "cliff", "desert", "earth", "flame", "garden", "hill", "island", "jungle", "kettle", "lake", "mountain", "ocean", "planet", "river", "sun", "tree", "volcano", "waterfall", "xeric", "yard", "zoo", "actor", "book", "cell", "drum", "film", "guitar", "harp", "ink", "jar", "keyboard", "light", "mirror", "note", "organ", "paint", "question", "ring", "screen", "trumpet", "utensil", "violin", "wheel", "xerox", "yarn", "zip", "army", "bridge", "castle", "dam", "engine", "fort", "gate", "harbor", "infantry", "jet", "knife", "ladder", "missile", "navy", "outpost", "patrol", "queen", "radar", "soldier", "tank", "uniform", "vessel", "war", "xray", "yacht", "zone", "acid", "base", "carbon", "DNA", "electron", "fuel", "gas", "hydrogen", "ion", "jewel", "kinetic", "liquid", "molecule", "neutron", "oxygen", "proton", "quartz", "rock", "salt", "temperature", "uranium", "volume", "wave", "xenon", "yield", "zinc", "atom", "battery", "circuit", "diode", "energy", "frequency", "grid", "hertz", "inductor", "joule", "kilowatt", "load", "magnet", "node", "ohm", "power", "resistor", "switch", "transformer", "unit", "volt", "watt", "xform", "yield", "zap", "alias", "byte", "code", "data", "email", "file", "graph", "host", "internet", "join", "key", "link", "mode", "node", "object", "protocol", "query", "root", "server", "terminal", "URL", "virus", "zip"};
        String[] mediumWords = {"abrupt", "absorb", "accent", "access", "accomplice", "adjust", "admiral", "advance", "advise", "aerial", "agency", "airship", "algebra", "alight", "allege", "ambush", "amend", "amount", "anarchy", "anchor", "ancient", "animate", "annual", "anthem", "apathy", "appeal", "appoint", "aquatic", "arctic", "ardent", "arrange", "artist", "ascent", "ashore", "aspect", "aspire", "assault", "assert", "assign", "assist", "asylum", "attain", "attempt", "attract", "augment", "avenge", "aviator", "balance", "bantam", "barren", "beacon", "benign", "betray", "bewilder", "biscuit", "blatant", "blizzard", "bombard", "bonfire", "border", "bough", "breach", "brigade", "bronze", "buffet", "bureau", "burrow", "bustle", "cajole", "canvas", "caprice", "capture", "career", "carol", "cascade", "casual", "cavern", "cease", "census", "chagrin", "change", "chapel", "charge", "cherish", "choice", "choose", "chorus", "cipher", "clamor", "clause", "cleave", "client", "climax", "clink", "closet", "cluster", "coalesce", "coerce", "colony", "combat", "combine", "commend", "comment", "commit", "compact", "compare", "compel", "compete", "complex", "comply", "compose", "conceal", "concede", "concept", "concern", "concede", "confess", "confide", "conform", "confront", "confuse", "congest", "conjure", "connect", "consent", "consist", "console", "consume", "contact", "contain", "contest", "context", "contract", "contrast", "control", "convert", "convey", "convict", "copious", "correct", "corrupt", "council", "counter", "couple", "course", "covert", "cower", "create", "credit", "crimson", "crisis", "critic", "cross", "crucial", "crunch", "cryptic", "crystal", "cubicle", "culture", "curfew", "curtain", "cushion", "custom", "danger", "daring", "darken", "dawdle", "dazzle", "deadly", "debate", "debris", "debtor", "decent", "decide", "decree", "deduce", "defect", "defend", "define", "deftly", "degree", "deject", "delay", "delete", "delight", "deliver", "demand", "demise", "denote", "depart", "depend", "depict", "deprive", "derive", "descent", "describe", "desist", "desolate", "despair", "despise"};
        String[] difficultWords = {"Abyss", "Aplomb", "Aqueduct", "Asphyxiate", "Bacterium", "Baffling", "Banquet", "Bazaar", "Beryllium", "Bilirubin", "Binocular", "Blasphemy", "Blatant", "Bludgeon", "Brevity", "Bribery", "Buccaneer", "Buffoonery", "Bullion", "Bungalow", "Burglary", "Cacophony", "Calamity", "Camaraderie", "Camouflage", "Cataclysm", "Catastrophe", "Cavalry", "Cellophane", "Censorship", "Chivalry", "Chlorophyll", "Choreography", "Chromium", "Clavicle", "Clipboard", "Coalesce", "Coaxial", "Cobblestone", "Cockamamie", "Cockatoo", "Colloquial", "Commemorate", "Commensurate", "Compartment", "Congruent", "Connoisseur", "Conundrum", "Copious", "Corollary", "Counterfeit", "Credulous", "Cryptic", "Cubicle", "Culminate", "Cumbersome", "Curriculum", "Dastardly", "Daunting", "Debilitate", "Decipher", "Declamation", "Dehydrate", "Dejected", "Delirious", "Deluge", "Demagogue", "Despondent", "Dexterity", "Diametrically", "Dichotomy", "Dictatorship", "Dilapidated", "Diphthong", "Discombobulate", "Disingenuous", "Disparate", "Dodecahedron", "Dogmatic", "Doppelganger", "Draconian", "Dystopia", "Eccentric", "Eclectic", "Effervescent", "Egregious", "Elicit", "Elitist", "Elliptical", "Elongate", "Emaciate", "Embellish", "Enigmatic", "Enunciate", "Epiphany", "Epitome", "Equivocal", "Ethereal", "Euphemism", "Exacerbate", "Excruciating", "Exonerate", "Exorbitant", "Expedite", "Exquisite", "Facetious", "Fallacy", "Fastidious", "Fathom", "Felonious", "Ferocious", "Fictitious", "Flabbergast", "Flagrant", "Flippant", "Flourish", "Fluctuate", "Formidable", "Frivolous", "Frustrate", "Fugacious", "Garrulous", "Gelatinous", "Genial", "Gesticulate", "Ghastly", "Gluttony", "Grandiloquent", "Gratuitous", "Gregarious", "Grievous", "Grueling", "Hallucinate", "Harangue", "Harbinger", "Heinous", "Hemorrhage", "Heterogeneous", "Hindrance", "Hippopotamus", "Homogeneous", "Hyperbolic", "Hypochondriac", "Iconoclast", "Idiosyncratic", "Ignominious", "Illustrious", "Immaculate", "Impertinent", "Implacable", "Impregnable", "Inauspicious", "Incontrovertible", "Incorrigible", "Ineffable", "Ineptitude", "Inexorable", "Infinitesimal", "Infringe", "Iniquitous", "Insidious", "Insipid", "Insurmountable", "Intangible", "Interloper", "Intermittent", "Intransigent", "Intrinsic", "Inundate", "Inveterate", "Iridescent", "Juxtapose", "Labyrinth", "Lackadaisical", "Languid", "Lethargic", "Litigious", "Loquacious", "Lugubrious", "Magnanimous", "Malignant", "Malodorous", "Maudlin", "Melancholy", "Mendacious", "Metamorphosis", "Meticulous", "Microcosm", "Misanthrope", "Misconstrue", "Mollify", "Monotonous", "Multifarious", "Mundane", "Myriad", "Nebulous", "Nefarious", "Nomenclature", "Nonchalant", "Nondescript"};
        String[] wordArray = new String[]{"null"};

        switch (difficulty) {
            case 0:
                wordArray = easyWords;
                break;
            case 1:
                wordArray = mediumWords;
                break;
            case 2:
                wordArray = difficultWords;
                break;
            default:
                System.out.println("Invalid difficulty level");
        }

        int randomIndex = (int) (Math.random() * wordArray.length);

        return wordArray[randomIndex];
    }
}