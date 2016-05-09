package com.group16.seeyaapp.communication;

/**
 * Created by Andrea on 10/04/16.
 * Shared with server application to interpret
 * and create JSON communication
 * @author Gustav Frigren
 */

public final class ComConstants {

    // TYPE, nyckel som ska finnas med i varje JSON-objekt/sträng för identifiering av vilken slags meddelande
    // eller kommando objektet/filen innehåller.
    public static final String TYPE = "1";

    // TYPE för appens begäran om att registrera en ny användare.
    public static final String NEWUSER = "2";

    // TYPE för appens begäran om att logga in en användare.
    public static final String LOGIN ="3";

    // TYPE för appens begäran om att få en activity. Detta sker när användaren trycker på en headline i appen
    // för att se själva aktiviteten.
    public static final String ACTIVITIY = "4";

    public static final String ACTIVITY_HEADLINES = "5"; // TODO remove?

    // Key vid appens begäran om att logga in eller skapa ny användare.
    public static final String USERNAME = "6";

    // Key vid appens begäran om att logga in eller skapa ny användare.
    public static final String PASSWORD = "7";

    // Key vid appens begäran om att skapa ny användare.
    public static final String EMAIL = "8";

    public static final String CONFIRMATION = "9";			//TODO remove?

    // Key vid sändande av annat än standardmeddelande.
    public static final String MESSAGE = "10";
    public static final String CONFIRMATION_TYPE = "11";	//TODO remove?

    // TYPE vid felmeddelande från servern. Fins flera felmeddelanden. Ska möjligen tas bort.
    public static final String ERROR = "12";
    public static final String ERROR_TYPE = "13";			//TODO remove?

    // TYPE vid appens begäran om att skapa ny aktivitet.
    public static final String NEWACTIVITY = "14";

    // Key vid tex appens begäran om att skapa ny aktivitet.
    public static final String NAME = "15";

    // Key vid appens begäran om att skapa ny aktivitet. Används för platsen där aktiviteten ska utföras.
    public static final String PLACE = "16";

    // Key vid appens begäran om att skapa ny aktivitet. Används för starttiden för aktiviteten.
    public static final String TIME = "17";

    // Key vid appens begäran om att skapa ny aktivitet. Används för att ange max antal deltagare i aktiviteten.
    public static final String MAX_NBROF_PARTICIPANTS = "18";

    // Key vid angivandet av subkategori
    public static final String SUBCATEGORY = "19";

    // Key vid angivandet av minsta antalet deltagare. Används av appen vid skapandet av ny aktivitet.
    public static final String MIN_NBR_OF_PARTICIPANTS = "20";

    // TYPE för appens begäran om att publisera en aktivitet till alla användare av systemet, publik
    public static final String PUBLISH_ACTIVITY = "21";

    // Key för angivandet av något id, tex aktivitets-id.
    public static final String ID = "22";

    // Key för angivandet av datum.
    public static final String DATE = "23";

    // Key för angivandet av headline/rubrik på aktivitet.
    public static final String HEADLINE = "24";

    // Key för angivandet av den användare som skapat/äger aktiviteten.
    public static final String ACTIVITY_OWNER = "25";


    public static final String ACTIVITY_CATEGORIES = "26";
    public static final String LOCATIONS = "27";
    public static final String MAINCATEGORY = "28";
    public static final String LOCATIONS_VERSION_NBR = "29";

    //
    public static final String ARRAY_SUBCATEGORY = "30";

    // Key för serverns retur av huvudkategorier i form av en array. Ingår i svar på appens begäran om
    // GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER och
    // GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES
    public static final String ARRAY_MAINCATEGORY = "31";

    // Key för serverns retur av headlines i form av en array. Ingår i svar på appens begäran om
    // GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER och
    // GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES
    public static final String ARRAY_HEADLINE = "32";
    public static final String NBR_OF_SIGNEDUP = "33";
    public static final String MY_ACTIVITIES = "34";
    public static final String ARRAY_LANDSCAPE = "35";
    public static final String ARRAY_CITY = "36";
    public static final String LOGIN_OK = "37";
    public static final String LOGIN_FAIL = "38";
    public static final String DATE_PUBLISHED = "39";
    public static final String LOCATIONS_CONFIRMATION = "40";
    public static final String CATEGORIES_CONFIRMATION = "41";
    public static final String CATEGORIES_VERSION_NUMBER = "42";

    // TYPE för appens begäran am att en användare ska delta i en aktivitet.
    public static final String SIGNUP = "43";

    // TYPE för appens begäran om att en användare ska avregisterera sig från en aktivitet.
    public static final String UNREGISTER_FROM_ACTIVITY = "44";

    // TYPE för serverns svar på att en användare anmälts till en aktivitet.
    public static final String SIGNUP_CONFIRMATION = "45";

    // TYPE för serverns svar på att en användare inte kunde anmälas till en aktivitet.
    public static final String SIGNUP_ERROR = "46";

    // TYPE för serverns svar på att en aktivitet har publicerats. Svar på appens begäran om PUBLISH_ACTIVITY
    // eller PUBLISH_ACTIVITY_TO_SPECIFIC_USERS
    public static final String PUBLISH_ACTIVITY_CONFIRMATION = "47";

    // TYPE för servens svar på att en aktivitet inte lyckats publiceras. Svar på appens begäran om PUBLISH_ACTIVITY
    // eller PUBLISH_ACTIVITY_TO_SPECIFIC_USERS
    public static final String PUBLISH_ACTIVITY_ERROR = "48";

    // TYPE för servens svar på att en ny aktivitet har skapats. Svar på appens begäran om NEWACTIVITY
    public static final String NEW_ACTIVTIY_CONFIRMATION = "49";

    // TYPE för serverns svar på att en ny aktivitet inte lyckats skapas. Svar på appens begäran om NEWACTIVITY
    public static final String NEW_ACTIVITY_ERROR = "50";

    // TYPE för serverns svar på att en ny användare har skapats. Svar på appens begäran om NEWUSER
    public static final String NEW_USER_CONFIRMATION = "51";

    // TYPE för serverns svar på att en ny användare inte lyckades skapas. Svar på appens begäran om NEWUSER
    public static final String NEW_USER_ERROR = "52";

    //TYPE för appens begäran om att få huvudkategori, subkategori och aktivitetsrubriker för en viss användare.
    // där användaren är inbjuden till aktiviteter.
    public static final String GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER = "53";

    //TYPE för appens begäran om att få huvudkategori, subkategori och aktivitetsrubriker för en viss användare.
    // där användaren har skapat/äger aktiviteterna.
    public static final String GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES = "54";

    //TYPE för svar från servern på begäran av typ: GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER.
    public static final String MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER = "55";

    //TYPE för svar från servern på begäran av typ: GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES.
    public static final String MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES = "56";

    // TYPE för appens begäran om att publisera aktivitet för andra än alla användare, dvs till utpekade användare
    public static final String PUBLISH_ACTIVITY_TO_SPECIFIC_USERS = "57";

    // TYPE för appens begäran om att kontrollera om en användare finns i databasen.
    public static final String CHECK_IF_USER_EXISTS = "58";

    // TYPE för serverns svar på frågan CHECK_IF_USER_EXISTS, om användaren finns i databasen.
    public static final String USER_EXISTS = "59";

    // TYPE för serverns svar på frågan CHECK_IF_USER_EXISTS, om användaren inte finns i databasen.
    public static final String INVALID_USERNAME = "60";

    // TYPE för serverns svar på appens begäran om att avregistera en användare från en aktivitet, om detta gått bra.
    public static final String UNREGISTER_FROM_ACTIVITY_CONFIRMATION = "61";

    // TYPE för serverns svar på appens begäran om att avregistrera en användare från en aktivite, om detta inte gått bra.
    public static final String UNREGISTER_FROM_ACTIVITY_ERROR = "62";

    // Används tillsammans med commando/TYPE PUBLISH_ACTIVITY_TO_SPECIFIC_USERS. Innehåller en array med
    // de användare som aktiviteten ska publiseras till.
    public static final String ARRAY_USERNAME = "63";

    // Används av servern som nyckel för om en användare är signed up för en aktivitet eller inte.
    public static final String SIGNED_UP = "64";

    // Används som value till SIGNED_UP om användaren är signed up för aktiviteten.
    public static final String YES = "65";

    // Används som value till SIGNED_UP om användaren inte är signed up för aktiviteten.
    public static final String NO = "66";

    public static final String LOG_INFO = "INFO";
    public static final String LOG_ERROR = "ERROR";
}