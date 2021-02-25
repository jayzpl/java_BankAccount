package com.company;//Separacja klasy wykonujacej obliczenia (Obliczenia) i klasy
//Graficznego Interfejsu

import java .awt.* ;
import java.awt.event.* ;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

class Obliczenia{
    public int oblicz1(int n) { return n+10 ; }
    public int oblicz2(int n) { return n-1 ; }
}

class Konto implements  Serializable{   //+klasa pośrednicząca w zapisie i odczycie danych
    public float stan_konta;    //+wartości ktore zapisujemy
    public String stan_konta_string;
    public float ostatnia_operacja;
    public ArrayList<String> historia_operacji;
    public int ilosc_operacji;

    Konto(float kwota_float, String kwota_string, float ostatnie_operacje, ArrayList historia, int licznik_operacji ){ //+konstruktor przy stworzeniu obiektu dajemy mu wartości z aplikacji
        stan_konta = kwota_float;
        stan_konta_string = kwota_string;
        ostatnia_operacja = ostatnie_operacje;
        historia_operacji = historia;
        ilosc_operacji = licznik_operacji;
    }
}

class GIdoObl extends JFrame  {
    float kwota_float = 100; //poczatkowy stan konta
    String kwota_string;     //poczatkowy stan konta ale string
    float ostatnie_operacje; //+kwota przed wykonaniem operacji
    ArrayList<String> historia = new ArrayList<String>(); //+historia wykonanych tranzakcji
    int licznik_operacji=0; //+licznik wykonanych tranzakcji

    JTextField
            dane  = new JTextField(20),
            wynik = new JTextField(20),
            pieniadze = new JTextField(20), //textfield ze stanem konta
            kwota = new JTextField(20),     //textfield z kwota jaka chcemy wplacic / wyplacic
            rezultat = new JTextField(20);  //textfield z rezultatem

    Obliczenia ob = new Obliczenia() ;

    JButton
            obl1 = new JButton("obliczenie 1") ,
            obl2 = new JButton("obliczenie 2") ,
            //======
            wplata = new JButton("Wplata / Wyplata"),   //button wplata/wyplata
            odblokuj = new JButton("Odblokuj"),        //button odblokuj
            cofnij = new JButton("OCofnij ostatnia operacje"), //+button cofnij
            zapis = new JButton("Zapis do pliku"),      //+button zapis
            odczyt = new JButton("Odczyt z pliku"),     //+button odczyt
            historiaB = new JButton("Pokaz Historie");  //+button historia tranzakcji

    GIdoObl(){
        setTitle("GI do Obliczen");
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(9,2,10,10)) ; //zwiekszamy wiersze aby zmiescily sie nowe elementy
        cp.add(new JLabel("Argument:")) ;
        cp.add(dane) ;
        obl1.addActionListener(new Obl1L());
        obl2.addActionListener(new Obl2L());
        cp.add(obl1) ;
        cp.add(obl2) ;
        cp.add(new JLabel("")) ; // odstep
        cp.add(new JLabel("")) ;
        cp.add(new JLabel("Wynik:")) ;
        cp.add(wynik) ;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true) ;
        //==============
        //tutaj zaczynaja sie rzeczy z modyfikacja konta


        cp.add(new JLabel("Stan:")) ;           //label stan
        kwota_string = String.valueOf(kwota_float);  //rzutujemy float ze stanem konta na string
        pieniadze.setText(kwota_string);             //ustawiamy wartosc textfield ze stanem konta na dana kwote
        cp.add(pieniadze) ;                          //dodanie pol na plansze
        cp.add(wplata) ;
        cp.add(kwota) ;
        cp.add(rezultat) ;
        cp.add(odblokuj) ;
        cp.add(cofnij) ;
        cp.add(historiaB);
        cp.add(zapis);
        cp.add(odczyt);
        odblokuj.addActionListener(new Odbl());     //akcja Odbl() dla buttona odblokuj
        wplata.addActionListener(new Wplata());     //akcja Wplata() dla buttona wplata
        cofnij.addActionListener(new Cofnij());     //+akcja Cofnij() dla buttona cofnij
        zapis.addActionListener(new Zapis());       //+akcja Zapis() dla buttona zapis
        odczyt.addActionListener(new Odczyt());     //+akcja Odczyt() dla buttona odczyt
        historiaB.addActionListener(new HistoriaB());//+akcja HistoriaB() dla buttona historia
    }
    int dajLiczbe(JTextField tf){
        try{
            return Integer.parseInt(tf.getText()) ;
        } catch (NumberFormatException e){ return 0 ; }
    }
    class Obl1L implements ActionListener{
        public void actionPerformed(ActionEvent e){
            wynik.setText(Integer.toString(
                    ob.oblicz1(dajLiczbe(dane)))) ;
        }
    }
    class Obl2L implements ActionListener{
        public void actionPerformed(ActionEvent e){
            wynik.setText(Integer.toString(
                    ob.oblicz2(dajLiczbe(dane)))) ;
        }
    }
    //======
    class HistoriaB implements  ActionListener{     //+przy kliknieciu guzika wyswietli historie tranzakcji. Historia jest wypisywana w konsoli
        public void actionPerformed(ActionEvent e){
            for(int i=0; i<licznik_operacji; i++){  //+od 0 do tyle ile zostalo wykonanych operacji
                System.out.println(historia.get(i));
            }
            System.out.println("------");
        }
    }
    class Odbl implements ActionListener{           //akcja Odbl
        public void actionPerformed(ActionEvent e){
            kwota.setEditable(true);               //ustawienie edycji textfield kwota na true
            wplata.setEnabled(true);               //ustawienie edycji buttona wplata na true
            rezultat.setText("");                  //ustawienie pustego textfield rezultat
            kwota.setText("");                     //ustawienie pustego textfield kwota
        }
    }
    class Wplata implements ActionListener{

        public void actionPerformed(ActionEvent e){
            kwota_string = pieniadze.getText();                             //pobranie wartosci z textfield pieniadze (aktualny stan konta)
            try {   //+probujemy wykonac instrukcje ponizej
                kwota_float = Float.valueOf(kwota_string);                      //rzutowanie na float stringa z kwota
                float kwota_robocza;                                            //float kwoty ktora bedziemy wplacac/wyplacac
                String kwota_robocza_str;                                       //string kwoty ktora bedziemy wplacac/wyplacac
                kwota_robocza_str = kwota.getText();                            //pobranie wartosci z textfielda z kwota ktora bedziemy wplacac / wyplacac (+cos oznacza wplate -cos oznacza wyplate)
                kwota_robocza = Float.valueOf(kwota_robocza_str);               //rzutowanie na float stringa z kwota ktora bedziemy wplacac / wyplacac

                if (kwota_float + kwota_robocza >= 0) {                                //jesli nas na to stac to rob:
                    kwota.setEditable(false);                                   //ustawienie edycji textfielda kwota na false
                    wplata.setEnabled(false);                                   //ustawienie buttona wplata na false (wylaczenie go)

                    ostatnie_operacje = kwota_float;        //+zapisujemy kwote przed wykonaniem tranzakcji tak zeby mozna bylo ja potem cofnac
                    licznik_operacji++;                     //+zwiekszamy licznik wykonanych operacji

                    rezultat.setText("OK");                                     //ustawienie tekstu w textfield rezultat
                    String s = String.valueOf(kwota_robocza + kwota_float);       //ustawienie sumy kwoty ktora wplacamy / wyplacamy i naszego stanu konta (string)
                    pieniadze.setText(s);                                       //ustawienie textfielda pieniadze (nasz stan konta) na nowa sume

                    historia.add("Stan_przed: " + kwota_string + " Stan_po: " + s);     //+zapisanie stringa do arraylisty ze stanem przed i po operacji
                    kwota_float = Float.valueOf(s);                                     //+rzutowanie nowej kwoty po operacji ze stringa na float do zmiennej kwota_float
                    kwota_string = s;                                                   //+przypisanie nowej kwoty do zmiennej kwota_string
                } else {                                                          //jesli nas na to nie stac
                    float o_ile = -1 * (kwota_float + kwota_robocza);           //dodatnia roznica o ile pieniedzy chcemy za duzo wyplacic
                    String o_ile_str = String.valueOf(o_ile);                   //rzutowanie wartosci o_ile (float) na string
                    rezultat.setText("Brak srodkow, za duzo o: " + o_ile_str);  //wypisanie wartosci w textfield rezultat
                    historia.add("Operacja nie udana");                         //+stringa do arraylisty o nieudanej operacji
                    licznik_operacji++;                                         //+zwiekszenie licznika wykonanych operacji
                }
            }
            catch(NumberFormatException a){     //+jesli wystapi blad np kwota bedzie zawierac litery wylapujemy go
                rezultat.setText("Wpisana wartosc nie jest liczba"); //+wypisujemy rezultat o bledzie
            }
        }
    }

    class Cofnij implements  ActionListener{        //+cofnij powoduje powrot stanu konta do kwoty przed wykonaniem ostatniej operacji
        public void actionPerformed(ActionEvent e){
            String s = String.valueOf(ostatnie_operacje);   //+przypisujemy zmiennej s tekst rzutowany z float z ostatniej kwoty zapisanej przed wykonaniem operacji
            pieniadze.setText(s);                           //+ustawiamy pole tekstowe ze stanem konta na stara kwote
            kwota_float = ostatnie_operacje;                //+ustawiamy kwote na stara kwote
            rezultat.setText("Operacja cofnieta");          //+wypisujemy rezultat
            historia.add("Operacja cofnieta stan: "+s);     //+dodajemy stringa do historii tranzakcji
            licznik_operacji++;                             //+zwiekszamy licznik wykonanych operacji
        }
    }
    class Zapis implements  ActionListener{                 //+powoduje zapisu stanu aplikacji do plikku przy uzyciu serializacji
        public void actionPerformed(ActionEvent e){
            System.out.println("Zapiskwota: "+kwota_float);     //+pomocnicze wypisanie wiadomosci w konsoli
            Konto A = new Konto(kwota_float, kwota_string, ostatnie_operacje, historia, licznik_operacji); //+tworzymy obiekt z pomocniczej klasy Konto z pirzypisaniem do niego wartości z naszej aplikacji
            System.out.println(A.stan_konta);   //+pomocnicze wypisanie wiadomosci w konsoli

            try{    //+probujemy zapisac plik
                FileOutputStream f = new FileOutputStream("ObiektKonto");   //+tworzenie obiektu z klasy FileOutputStream z podaniem "na sztywno" nazwy pliku
                ObjectOutputStream os = new ObjectOutputStream(f);              //+tworzenie obiektu z klasy ObjectOutputStream przekazanie mu obiektu f
                os.writeObject(A);                                              //+zapisanie obiektu przy uzyciu metody writeObject
                f.close();                                                      //+zamkniecie pliku
                System.out.println("zapisane");                                 //+pomocnicze wypisanie wiadomosci w konsoli
            }catch (IOException s){}                                            //+wylapanie bledow wejscia/wyjscia

        }
    }
    class Odczyt implements  ActionListener{                    //+odczyt powoduje odczyt dancyh z pliku do aplikacji
        public void actionPerformed(ActionEvent e){
            Konto a = new Konto(100, "100.0", 0, null, 0);  //+utworzenie pomocniczego obiektu z klasy Konto z domyslnymi wartosciami startowymi
                                                                                                                //+jesli operacja odczytu sie nie uda to te wartosci zostana wczytane
            try{
                ObjectInputStream is = new ObjectInputStream(new FileInputStream("ObiektKonto")); //+tworzymy obiekt z klasy ObjectInputStream przekazujac mu obiekt z klasy FileInputStream z nazwa pliku
                a = (Konto)is.readObject();             //+nadpisujemy dane obiektu a danymi zczytanymi z pliku
                is.close();                             //+zamykamy plik
            }
            catch (IOException s){                      //+wylapanie bledow wejscia/wyjscia
                System.out.println("cos nie dziala");   //+pomocnicze wypisanie wiadomosci w konsoli
            }catch (ClassNotFoundException s){}         //+wylapanie bledow przy nieznalezionym pliku

            kwota_float = a.stan_konta;                 //+wpisanie danych z pomocniczego obiektu do zmiennych naszej aplikacji
            System.out.println("stan: "+a.stan_konta);
            kwota_string = a.stan_konta_string;
            ostatnie_operacje = a.ostatnia_operacja;
            historia = a.historia_operacji;
            licznik_operacji = a.ilosc_operacji;
            pieniadze.setText(kwota_string);
            rezultat.setText("Wczytane");
        }
    }
//======


    public static void main(String[] arg){
        JFrame gi = new GIdoObl() ;
        gi.setSize(200,200) ;
    }
}
class DebetException extends Exception {}

class Konto2{
    private int stan;
    Konto2() { stan = 0; }
    public void operacja(int ile) throws DebetException {
        if (stan + ile >= 0 )
            stan += ile;
        else
            throw new DebetException();
    }
    public int dajStan() { return stan ; }

}
