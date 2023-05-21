Stworzyłem trzy klasy. 
Main, do uruchomienia funkcji i przypisania ścieżek. 
Period, która jest klasą, której obiekty posiadają początek i koniec w czasie.
Calendar, czyli klasę "główną" posiadającą statyczne metody do read'a plików json i wyszukania możliwych terminów spotkania.

Nie było informacji o formacie plików. Zamiast używać .txt, wybrałem .json z uwagi na łatwiejsze wyciągnięcie informacji dzięki dedykowanym bibliotekom.
Wymagało to tylko drobnych przeróbek z apostrofami

Kalendarze znajdują się w folderze projektu Calendars

Kod nieidealny pod względem obsługi błędów ze strony użytkownika jeśli chodzi o podanie niepoprawnych danych. Jest na to delikatny np. meetingDuration.

Dodałem dużą ilość komentarzy, w języku angielskim, samym kodzie wyjaśniające działanie krok po kroku
