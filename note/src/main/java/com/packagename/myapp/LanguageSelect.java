package com.packagename.myapp;

public class LanguageSelect
{
    public enum Language{
        BUTTON_CLEAR(0), BUTTON_EXPORT(1), UNFINISHED(2), FINISHED(3),
        SORT_DATE(4), SORT_TITLE(5), CATEGORIES(6), DATE_FROM(7),
        DATE_UNTIL(8), WRITE_HERE(9), SAVE_NOTE(10),
        APPLY_FILTER(11), EDIT(12), DELETE(13), SHARE(14),
        DONE(15), SAVE(16), SHARE_WITH(17), VALID_EMAIL(18),
        SEND(19), CLOSE(20), PRIORITY(21), PRIORITY_FILLING(22),
        CATEGORY(23), CATEGORY_FILLING(24), SCHOOL(25), SHOPPING(26),
        HOME(27), WORK(28), WORKOUT(29), TITLE(30), IMPORT(31), EXPORT(32);

        int index;

        Language(int newValue) {
            index = newValue;
        }

        public int getIndex()
        {
            return index;
        }
    }

    static String[][] languages = {
            {"Löschen", "Clear", "Effacer"},
            {"Exportieren", "Export", "Exportation"},
            {"Zeige Vollendet", "Show finished", "Spectacle terminé"},
            {"Zeige Unvollendete", "Show Unfinished", "Afficher inachevé"},
            {"Sortiere nach Datum", "Sort Date", "Date de tri"},
            {"Sortiere nach Titel", "Sort Title", "Trier le titre"},
            {"Kategorien", "Categories", "Les catégories"},
            {"Datum von", "Date from", "Dater de"},
            {"Datum bis", "Date until", "Date jusqu'au"},
            {"Hier schreiben...", "Write here...", "Ecrire ici..."},
            {"Notiz speichern", "Save note", "Enregistrer la note"},
            {"Filter anwenden", "Apply filter", "Appliquer le filtre"},
            {"Ändern", "Edit", "Éditer"},
            {"Löschen", "Delete", "Supprimer"},
            {"Teilen", "Share", "Partager"},
            {"Fertig", "Done", "Terminé"},
            {"Speichern", "Save", "sauver"},
            {"Teilen mit: (email)", "share with: (email)", "partager avec: (email)"},
            {"Bitte geben Sie eine gültige E-Mail-Adresse ein.", "Please enter a valid email adress.", "Veuillez saisir une adresse e-mail valide."},
            {"Senden", "Send", "Envoyer"},
            {"Schließen", "Close", "proche"},
            {"Priorität", "Priority", "Priorité"},
            {"Priorität muss ausgefüllt werden!", "Priority must be filled in!", "La priorité doit être remplie!"},
            {"Kategorie:", "Category:", "Catégorie:"},
            {"Kategorie muss ausgefüllt werden!", "Category must be filled in!", "La catégorie doit être remplie!"},
            {"Schule", "School", "École"},
            {"Einkaufen", "Shopping", "Achats"},
            {"Zuhause", "Home", "Accueil"},
            {"Arbeiten", "Work", "Travail"},
            {"Training", "Workout", "Faire des exercices"},
            {"Titel", "Title", "Titre"},
            {"Importieren", "Import", "Importation"},
            {"Exportieren", "Export", "Exportation"},
    };
}
