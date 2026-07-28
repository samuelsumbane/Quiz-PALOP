package com.samuelsumbane.quizpalop.data.repository

import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.CountryInfo

val countriesData = listOf(
    CountryInfo(
        country = Countries.Angola,
        flagPath = R.drawable.minangola,
        capital = "Luanda",
        corrency = "Kwanza (AOA)",
        independencia = "11 de novembro de 1975",
        area = "1 246 700 km²",
        paisesVisinhos = "Namíbia, República Democrática do Congo, Zâmbia e República do Congo (através do enclave de Cabinda)",
        fusoHorario = "UTC+1"
    ),
    CountryInfo(
        country = Countries.Cv,
        flagPath = R.drawable.capeverde,
        capital = "Praia",
        corrency = "Escudo Cabo-verdiano (CVE)",
        independencia = "5 de julho de 1975",
        area = "4 033 km²",
        paisesVisinhos = "Arquipélago no oceano Atlântico",
        fusoHorario = "UTC-1"
    ),
    CountryInfo(
        country = Countries.Gw,
        flagPath = R.drawable.guineabissau,
        capital = "Bissau",
        corrency = "Franco CFA da África Ocidental (XOF)",
        independencia = "24 de setembro de 1973 (declarada) / reconhecida em 10 de setembro de 1974",
        area = "36 125 km²",
        paisesVisinhos = "Senegal e Guiné",
        fusoHorario = "UTC+0"
    ),CountryInfo(
        country = Countries.Mz,
        flagPath = R.drawable.minmozambique,
        capital = "Maputo",
        corrency = "Metical (MZN)",
        independencia = "25 de junho de 1975",
        area = "799 380 km²",
        paisesVisinhos = "Tanzânia, Malawi, Zâmbia, Zimbabwe, África do Sul e Eswatini",
        fusoHorario = "UTC+2"
    ),
    CountryInfo(
        country = Countries.Stp,
        flagPath = R.drawable.saotomeandprincipe,
        capital = "São Tomé",
        corrency = "Dobra (STN)",
        independencia = "12 de julho de 1975",
        area = "964 km²",
        paisesVisinhos = "Arquipélago no golfo da Guiné",
        fusoHorario = "UTC+0"
    ),
)