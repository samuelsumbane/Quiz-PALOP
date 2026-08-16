package org.quizpalop.app.data.repository

import org.quizpalop.app.R
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.CountryInfo

val countriesData = listOf(
    CountryInfo(
        country = Countries.Angola,
        flagPath = R.drawable.minangola,
        capital = "Luanda",
        currency = "Kwanza (AOA)",
        independencia = "11 de novembro de 1975",
        area = "1 246 700 km²",
        paisesVisinhos = "Namíbia, República Democrática do Congo, Zâmbia e República do Congo (através do enclave de Cabinda)",
        fusoHorario = "UTC+1"
    ),
    CountryInfo(
        country = Countries.Cv,
        flagPath = R.drawable.capeverde,
        capital = "Praia",
        currency = "Escudo Cabo-verdiano (CVE)",
        independencia = "5 de julho de 1975",
        area = "4 033 km²",
        paisesVisinhos = "Arquipélago no oceano Atlântico",
        fusoHorario = "UTC-1"
    ),
    CountryInfo(
        country = Countries.Gw,
        flagPath = R.drawable.guineabissau,
        capital = "Bissau",
        currency = "Franco CFA da África Ocidental (XOF)",
        independencia = "24 de setembro de 1973 (declarada) / reconhecida em 10 de setembro de 1974",
        area = "36 125 km²",
        paisesVisinhos = "Senegal e Guiné",
        fusoHorario = "UTC+0"
    ),CountryInfo(
        country = Countries.Mz,
        flagPath = R.drawable.minmozambique,
        capital = "Maputo",
        currency = "Metical (MZN)",
        independencia = "25 de junho de 1975",
        area = "799 380 km²",
        paisesVisinhos = "Tanzânia, Malawi, Zâmbia, Zimbabwe, África do Sul e Eswatini",
        fusoHorario = "UTC+2"
    ),
    CountryInfo(
        country = Countries.Stp,
        flagPath = R.drawable.saotomeandprincipe,
        capital = "São Tomé",
        currency = "Dobra (STN)",
        independencia = "12 de julho de 1975",
        area = "964 km²",
        paisesVisinhos = "Arquipélago no golfo da Guiné",
        fusoHorario = "UTC+0"
    ),
)