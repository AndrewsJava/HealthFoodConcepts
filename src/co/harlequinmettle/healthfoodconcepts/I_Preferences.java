package co.harlequinmettle.healthfoodconcepts;

public interface I_Preferences {
 
	static final float[] DEFAULT_NUTR_GOALS = {
		50F,       //  g   Protein
		130F,       //  g   Carbohydrate, by difference
		70F,       //  g   Total lipid (fat)
		300F,       //  mg   Cholesterol
		0.0f,      // g   Ash
		2000F,       //  kcal   Energy
		8368f,      // kJ   Energy
		3000F,       //  g   Water
		600F,       //  mg   Phytosterols
		200F,       //  mg   Stigmasterol
		200F,       //  mg   Campesterol
		200F,       //  mg   Beta-sitosterol
		500F,       //  mg   Betaine
		0F,       //  mg   Caffeine
		300F,       //  mg   Theobromine
		0.0f,      // g   Sugars, total
		35F,       //  g   Fiber, total dietary
		0.0f,      // g   Starch
		0.0f,      // g   Galactose
		0.0f,      // g   Sucrose
		0.0f,      // g   Glucose (dextrose)
		0.0f,      // g   Fructose
		0.0f,      // g   Lactose
		0.0f,      // g   Maltose
		30F,       //  g   Alcohol, ethyl
		1000F,       //  mg   Calcium, Ca
		8F,       //  mg   Iron, Fe
		350F,       //  mg   Magnesium, Mg
		700F,       //  mg   Phosphorus, P
		4700F,       //  mg   Potassium, K
		1500F,       //  mg   Sodium, Na
		11F,       //  mg   Zinc, Zn
		1F,       //  mg   Copper, Cu
		4000F,       //  micrograms   Fluoride, F
		2.3F,       //  mg   Manganese, Mn
		55F,       //  micrograms   Selenium, Se
		0.0f,      // IU   Vitamin A, IU
		0.0f,      // micrograms   Retinol
		900F,       //  micrograms   Vitamin A, RAE
		0.0f,      // micrograms   Carotene, beta
		0.0f,      // micrograms   Carotene, alpha
		15F,       //  mg   Vitamin E (alpha-tocopherol)
		0F,       //  IU   Vitamin D
		0.0f,      // micrograms   Vitamin D3 (cholecalciferol)
		15F,       //  micrograms   Vitamin D (D2 + D3)
		0F,       //  micrograms   Cryptoxanthin, beta
		250F,       //  micrograms   Lycopene
		5F,       //  micrograms   Lutein + zeaxanthin
		0.0f,      // mg   Tocopherol, beta
		0.0f,      // mg   Tocopherol, gamma
		0.0f,      // mg   Tocopherol, delta
		90F,       //  mg   Vitamin C, total ascorbic acid
		1.2F,       //  mg   Thiamin B-1
		1.3F,       //  mg   Riboflavin B-2
		16F,       //  mg   Niacin B-3
		5F,       //  mg   Pantothenic acid B-5
		1.3F,       //  mg   Vitamin B-6
		0.0f,      // micrograms   Folate, total
		2.4F,       //  micrograms   Vitamin B-12
		550F,       //  mg   Choline, total
		0.0f,      // micrograms   Menaquinone-4 (k)
		0F,       //  micrograms   Dihydrophylloquinone
		120F,       //  micrograms   Vitamin K (phylloquinone)
		0.0f,      // micrograms   Folic acid
		400F,       //  micrograms   Folate B-9, food
		0.0f,      // micrograms   Folate, DFE
		0.0f,      // g   Tryptophan
		0.0f,      // g   Threonine
		0.0f,      // g   Isoleucine
		0.0f,      // g   Leucine
		0.0f,      // g   Lysine
		0.0f,      // g   Methionine
		0.0f,      // g   Cystine
		0.0f,      // g   Phenylalanine
		0.0f,      // g   Tyrosine
		0.0f,      // g   Valine
		0.0f,      // g   Arginine
		0.0f,      // g   Histidine
		0.0f,      // g   Alanine
		0.0f,      // g   Aspartic acid
		0.0f,      // g   Glutamic acid
		0.0f,      // g   Glycine
		0.0f,      // g   Proline
		0.0f,      // g   Serine
		0.0f,      // g   Hydroxyproline
		0.5F,       //  g   20:5 n-3 (EPA)
		0.5F,       //  g   22:5 n-3 (DPA)
		0.5F,       //  g   22:6 n-3 (DHA)
		0.5F,       //  g   18:3 n-3 c,c,c (ALA)
		0.0f,      // g   20:3 n-3
		5F,       //  g   18:3 n-6 c,c,c
		5F,       //  g   18:2 n-6 c,c
		5F,       //  g   20:2 n-6 c,c
		0.0f,      // g   18:2 CLAs
		20F,       //  g   Fatty acids, total saturated
		0.0f,      // g   Fatty acids, total monounsaturated
		0.0f,      // g   Fatty acids, total polyunsaturated
		0.0f,      // g   Fatty acids, total trans
		0.0f,      // g   Fatty acids, total trans-monoenoic
		0.0f,      // g   Fatty acids, total trans-polyenoic
		0.0f,      // g   4:0
		0.0f,      // g   6:0
		0.0f,      // g   8:0
		0.0f,      // g   10:0
		0.0f,      // g   12:0
		0.0f,      // g   14:0
		0.0f,      // g   16:0
		0.0f,      // g   18:0
		0.0f,      // g   20:0
		0.0f,      // g   18:1 undifferentiated
		0.0f,      // g   18:2 undifferentiated
		0.0f,      // g   18:3 undifferentiated
		0.0f,      // g   20:4 undifferentiated
		0.0f,      // g   22:0
		0.0f,      // g   14:1
		0.0f,      // g   16:1 undifferentiated
		0.0f,      // g   18:4
		0.0f,      // g   20:1
		0.0f,      // g   15:0
		0.0f,      // g   17:0
		0.0f,      // g   24:0
		0.0f,      // g   18:1 t
		0.0f,      // g   18:2 t not further defined
		0.0f,      // g   24:1 c
		0.0f,      // g   16:1 c
		0.0f,      // g   18:1 c
		0.0f,      // g   22:1 c
		0.0f,      // g   17:1
		0.0f,      // g   20:3 undifferentiated
		0.0f,      // g   15:1
};
		static final boolean[] DEFAULT_GOOD_NUTRIENTS = {
		true , ////0   Protein
		true , ////1   Carbohydrate, by difference
		true , ////2   Total lipid (fat)
		false , ////3   Cholesterol
		true , ////4   Ash
		true , ////5   Energy
		true , ////6   Energy
		true , ////7   Water
		true , ////8   Phytosterols
		true , ////9   Stigmasterol
		true , ////10   Campesterol
		true , ////11   Beta-sitosterol
		true , ////12   Betaine
		true , ////13   Caffeine
		true , ////14   Theobromine
		false , ////15   Sugars, total
		true , ////16   Fiber, total dietary
		true , ////17   Starch
		true , ////18   Galactose
		false , ////19   Sucrose
		false , ////20   Glucose (dextrose)
		false , ////21   Fructose
		true , ////22   Lactose
		true , ////23   Maltose
		false , ////24   Alcohol, ethyl
		true , ////25   Calcium, Ca
		true , ////26   Iron, Fe
		true , ////27   Magnesium, Mg
		true , ////28   Phosphorus, P
		true , ////29   Potassium, K
		false , ////30   Sodium, Na
		true , ////31   Zinc, Zn
		true , ////32   Copper, Cu
		true , ////33   Fluoride, F
		true , ////34   Manganese, Mn
		true , ////35   Selenium, Se
		true , ////36   Vitamin A, IU
		true , ////37   Retinol
		true , ////38   Vitamin A, RAE
		true , ////39   Carotene, beta
		true , ////40   Carotene, alpha
		true , ////41   Vitamin E (alpha-tocopherol)
		true , ////42   Vitamin D
		true , ////43   Vitamin D3 (cholecalciferol)
		true , ////44   Vitamin D (D2 + D3)
		true , ////45   Cryptoxanthin, beta
		true , ////46   Lycopene
		true , ////47   Lutein + zeaxanthin
		true , ////48   Tocopherol, beta
		true , ////49   Tocopherol, gamma
		true , ////50   Tocopherol, delta
		true , ////51   Vitamin C, total ascorbic acid
		true , ////52   Thiamin
		true , ////53   Riboflavin
		true , ////54   Niacin
		true , ////55   Pantothenic acid
		true , ////56   Vitamin B-6
		true , ////57   Folate, total
		true , ////58   Vitamin B-12
		true , ////59   Choline, total
		true , ////60   Menaquinone-4
		false , ////61   Dihydrophylloquinone
		true , ////62   Vitamin K (phylloquinone)
		true , ////63   Folic acid
		true , ////64   Folate, food
		true , ////65   Folate, DFE
		true , ////66   Tryptophan
		true , ////67   Threonine
		true , ////68   Isoleucine
		true , ////69   Leucine
		true , ////70   Lysine
		true , ////71   Methionine
		true , ////72   Cystine
		true , ////73   Phenylalanine
		true , ////74   Tyrosine
		true , ////75   Valine
		true , ////76   Arginine
		true , ////77   Histidine
		true , ////78   Alanine
		true , ////79   Aspartic acid
		true , ////80   Glutamic acid
		true , ////81   Glycine
		true , ////82   Proline
		true , ////83   Serine
		true , ////84   Hydroxyproline
		true , ////85   20:5 n-3 (EPA)
		true , ////86   22:5 n-3 (DPA)
		true , ////87   22:6 n-3 (DHA)
		true , ////88   18:3 n-3 c,c,c (ALA)
		true , ////89   20:3 n-3
		true , ////90   18:3 n-6 c,c,c
		true , ////91   18:2 n-6 c,c
		true , ////92   20:2 n-6 c,c
		true , ////93   18:2 CLAs
		false , ////94   Fatty acids, total saturated
		true , ////95   Fatty acids, total monounsaturated
		true , ////96   Fatty acids, total polyunsaturated
		false , ////97   Fatty acids, total trans
		false , ////98   Fatty acids, total trans-monoenoic
		false , ////99   Fatty acids, total trans-polyenoic
		true , ////100   4:0
		true , ////101   6:0
		true , ////102   8:0
		true , ////103   10:0
		true , ////104   12:0
		true , ////105   14:0
		true , ////106   16:0
		true , ////107   18:0
		true , ////108   20:0
		true , ////109   18:1 undifferentiated
		true , ////110   18:2 undifferentiated
		true , ////111   18:3 undifferentiated
		true , ////112   20:4 undifferentiated
		true , ////113   22:0
		true , ////114   14:1
		true , ////115   16:1 undifferentiated
		true , ////116   18:4
		true , ////117   20:1
		true , ////118   15:0
		true , ////119   17:0
		true , ////120   24:0
		true , ////121   18:1 t
		true , ////122   18:2 t not further defined
		true , ////123   24:1 c
		true , ////124   16:1 c
		true , ////125   18:1 c
		true , ////126   22:1 c
		true , ////127   17:1
		true , ////128   20:3 undifferentiated
		true , ////129   15:1
	};
	static final boolean[] DEFAULT_NUTRIENTS = {
		true , ////0   Protein
		true , ////1   Carbohydrate, by difference
		true , ////2   Total lipid (fat)
		true , ////3   Cholesterol
		true , ////4   Ash
		true , ////5   Energy
		true , ////6   Energy
		true , ////7   Water
		true , ////8   Phytosterols
		true , ////9   Stigmasterol
		true , ////10   Campesterol
		true , ////11   Beta-sitosterol
		true , ////12   Betaine
		true , ////13   Caffeine
		true , ////14   Theobromine
		true , ////15   Sugars, total
		true , ////16   Fiber, total dietary
		true , ////17   Starch
		true , ////18   Galactose
		true , ////19   Sucrose
		true , ////20   Glucose (dextrose)
		true , ////21   Fructose
		true , ////22   Lactose
		true , ////23   Maltose
		true , ////24   Alcohol, ethyl
		true , ////25   Calcium, Ca
		true , ////26   Iron, Fe
		true , ////27   Magnesium, Mg
		true , ////28   Phosphorus, P
		true , ////29   Potassium, K
		true , ////30   Sodium, Na
		true , ////31   Zinc, Zn
		true , ////32   Copper, Cu
		true , ////33   Fluoride, F
		true , ////34   Manganese, Mn
		true , ////35   Selenium, Se
		true , ////36   Vitamin A, IU
		true , ////37   Retinol
		true , ////38   Vitamin A, RAE
		true , ////39   Carotene, beta
		true , ////40   Carotene, alpha
		true , ////41   Vitamin E (alpha-tocopherol)
		true , ////42   Vitamin D
		true , ////43   Vitamin D3 (cholecalciferol)
		true , ////44   Vitamin D (D2 + D3)
		true , ////45   Cryptoxanthin, beta
		true , ////46   Lycopene
		true , ////47   Lutein + zeaxanthin
		true , ////48   Tocopherol, beta
		true , ////49   Tocopherol, gamma
		true , ////50   Tocopherol, delta
		true , ////51   Vitamin C, total ascorbic acid
		true , ////52   Thiamin
		true , ////53   Riboflavin
		true , ////54   Niacin
		true , ////55   Pantothenic acid
		true , ////56   Vitamin B-6
		true , ////57   Folate, total
		true , ////58   Vitamin B-12
		true , ////59   Choline, total
		true , ////60   Menaquinone-4
		true , ////61   Dihydrophylloquinone
		true , ////62   Vitamin K (phylloquinone)
		true , ////63   Folic acid
		true , ////64   Folate, food
		true , ////65   Folate, DFE
		false , ////66   Tryptophan
		false , ////67   Threonine
		false , ////68   Isoleucine
		false , ////69   Leucine
		false , ////70   Lysine
		false , ////71   Methionine
		false , ////72   Cystine
		false , ////73   Phenylalanine
		false , ////74   Tyrosine
		false , ////75   Valine
		false , ////76   Arginine
		false , ////77   Histidine
		false , ////78   Alanine
		false , ////79   Aspartic acid
		false , ////80   Glutamic acid
		false , ////81   Glycine
		false , ////82   Proline
		false , ////83   Serine
		false , ////84   Hydroxyproline
		true , ////85   20:5 n-3 (EPA)
		true , ////86   22:5 n-3 (DPA)
		true , ////87   22:6 n-3 (DHA)
		true , ////88   18:3 n-3 c,c,c (ALA)
		true , ////89   20:3 n-3
		true , ////90   18:3 n-6 c,c,c
		true , ////91   18:2 n-6 c,c
		true , ////92   20:2 n-6 c,c
		true , ////93   18:2 CLAs
		true , ////94   Fatty acids, total saturated
		true , ////95   Fatty acids, total monounsaturated
		true , ////96   Fatty acids, total polyunsaturated
		true , ////97   Fatty acids, total trans
		true , ////98   Fatty acids, total trans-monoenoic
		true , ////99   Fatty acids, total trans-polyenoic
		false , ////100   4:0
		false , ////101   6:0
		false , ////102   8:0
		false , ////103   10:0
		false , ////104   12:0
		false , ////105   14:0
		false , ////106   16:0
		false , ////107   18:0
		false , ////108   20:0
		false , ////109   18:1 undifferentiated
		false , ////110   18:2 undifferentiated
		false , ////111   18:3 undifferentiated
		false , ////112   20:4 undifferentiated
		false , ////113   22:0
		false , ////114   14:1
		false , ////115   16:1 undifferentiated
		false , ////116   18:4
		false , ////117   20:1
		false , ////118   15:0
		false , ////119   17:0
		false , ////120   24:0
		false , ////121   18:1 t
		false , ////122   18:2 t not further defined
		false , ////123   24:1 c
		false , ////124   16:1 c
		false , ////125   18:1 c
		false , ////126   22:1 c
		false , ////127   17:1
		false , ////128   20:3 undifferentiated
		false , ////129   15:1
	};
	static final boolean[] DEFAULT_FOOD_GROUPS = {  
		
		false,//0~American Indian/Alaska Native Foods
		false,//1~Baby Foods
		true,//2~Baked Products
		true,//3~Beef Products
		true,//4~Beverages
		true,//5~Breakfast Cereals
		true,//6~Cereal Grains and Pasta
		true,//7~Dairy and Egg Products
		true,//8~Fast Foods
		true,//9~Fats and Oils
		true,//10~Finfish and Shellfish Products
		true,//11~Fruits and Fruit Juices
		true,//12~Lamb, Veal, and Game Products 
		true,//13~Legumes and Legume Products
		false,//14~Meals, Entrees, and Sidedishes
		true,//15~Nut and Seed Products
		false,//16~Pork Products
		true,//17~Poultry Products
		false,//18~Restaurant Foods
		false,//19~Sausages and Luncheon Meats
		false,//20~Snacks
		false,//21~Soups, Sauces, and Gravies
		true,//22~Spices and Herbs
		true,//23~Sweets
		true//24~Vegetables and Vegetable Products
	};
}
