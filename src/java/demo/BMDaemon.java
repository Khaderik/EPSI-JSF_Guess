/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author KhadDev
 */
@ManagedBean
@SessionScoped
public final class BMDaemon {

    private String msgValeurIncorrecte;
    ResourceBundle bundle;
    private int valMax;
    private int valMin;
    private String phrase;
    private int devinette;
    private int nbChoixUser;
    private int nbEssai;
    private String boutonRecommencerVisible;
    private Boolean isDisabled;
    private Partie partieCourante;
    private List<Partie> listeParties;

    //<editor-fold defaultstate="collapsed" desc="Accesseurs">
    public String getMsgValeurIncorrecte() {
        return msgValeurIncorrecte;
    }

    public void setMsgValeurIncorrecte(String msgValeurIncorrecte) {
        this.msgValeurIncorrecte = msgValeurIncorrecte;
    }
    
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public int getValMax() {
        return valMax;
    }

    public void setValMax(int valMax) {
        this.valMax = valMax;
    }

    public int getValMin() {
        return valMin;
    }

    public void setValMin(int valMin) {
        this.valMin = valMin;
    }

    public String getBoutonRecommencerVisible() {
        return boutonRecommencerVisible;
    }

    public void setBoutonRecommencerVisible(String boutonRecommencerVisible) {
        this.boutonRecommencerVisible = boutonRecommencerVisible;
    }

    public String getBoutonOkVisible() {
        return boutonOkVisible;
    }

    public void setBoutonOkVisible(String boutonOkVisible) {
        this.boutonOkVisible = boutonOkVisible;
    }
    private String boutonOkVisible;

    public int getNbChoixUser() {
        return nbChoixUser;
    }

    public void setNbChoixUser(int nbChoisi) {
        this.nbChoixUser = nbChoisi;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String Phrase) {
        this.phrase = Phrase;
    }
//</editor-fold>

    /**
     * Creates a new instance of BMDaemon
     */
    public BMDaemon() {
        bundle = ResourceBundle.getBundle("res/strings/MessagesUser",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        listeParties = new ArrayList<>();
        lancerPartie();
    }

    // pas top à revoir
    public void comparer() {
        boolean saveOk = listeParties.contains(partieCourante);
        String msgHisto;
        String message;
        String msgCoup = String.format(bundle.getString("msg_CoupN"), nbEssai);

        if (nbEssai <= 5) {
            if (nbChoixUser > devinette) {
                message = String.format(bundle.getString("msg_TropGrand"), nbChoixUser);
                setPhrase(msgCoup + message);
            } else if (nbChoixUser < devinette) {
                message = String.format(bundle.getString("msg_TropPetit"), nbChoixUser);
                setPhrase(msgCoup + message);
            } else if (nbChoixUser == devinette) {
                message = String.format(bundle.getString("msg_Victoire"), nbEssai, nbChoixUser);
                setPhrase(message);
                msgHisto = message;
                partieCourante.setResultat(String.format(bundle.getString("msg_Partie"), listeParties.size() + 1, msgHisto));
                listeParties.add(partieCourante);
                finDePartie();
            }
            nbEssai++;
        } else {
            message = String.format(bundle.getString("msg_NombreEssaiAtteint"));
            setPhrase(message);

            if (!saveOk) {
                msgHisto = String.format(bundle.getString("msg_Perdu"), devinette);
                partieCourante.setResultat(String.format(bundle.getString("msg_Partie"), listeParties.size() + 1, msgHisto));
                listeParties.add(partieCourante);
            }
            finDePartie();
        }
    }

    public List<Partie> getListeParties() {
        return listeParties;
    }

    public void setListeParties(List<Partie> listeParties) {
        this.listeParties = listeParties;
    }

    private void finDePartie() {
        boutonRecommencerVisible = "visible";
        boutonOkVisible = "hidden";
    }

    public void lancerPartie() {
        generePlage();
        do {
            devinette = (new Random().nextInt(valMax)) + 1;
        } while (devinette < valMin);
        phrase = String.format(bundle.getString("msg_Debut"), valMin, valMax);
        msgValeurIncorrecte = String.format(bundle.getString("msg_valeurIncorrecte"), valMin, valMax);
        boutonRecommencerVisible = "hidden";
        boutonOkVisible = "visible";
        nbEssai = 1;
        isDisabled = listeParties.isEmpty();
        partieCourante = new Partie();
    }

    public void reset() {
        listeParties.clear();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    private void generePlage() {
        int val;
        do {
            val = (new Random().nextInt(20)) + 1;
            valMin = (new Random().nextInt(20)) + 1;
        } while ((val == valMin) || ((valMin - val)) < 5);

        if (valMin < val) {
            valMax = val;
        } else {
            valMax = valMin;
            valMin = val;
        }
    }
}