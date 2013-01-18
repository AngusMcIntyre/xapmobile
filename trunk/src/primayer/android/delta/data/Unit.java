package primayer.android.delta.data;

public enum Unit
{
  None				(0, ""),
  LitresPerSecond		(1, "l/s"),
  LitresPerHour		(2, "l/h"),
  CubicMetresPerHour	(3, "m³/h"),
  Metres				(4, "m"),
  Bar					(5, "Bar"),
  MilliMetres			(6, "mm"),
  MilliBar			(7, "mBar"),
  CubicMetres			(8, "m³"),
  Litres				(9, "l"),
  MilliGramsPerLitre	(10, "mg/l"),
  Hazen				(11, "HU"),
  NTU					(12, "NTU"),
  DegreesCentigrade	(13, "°C"),
  DegreesFarenheit	(14, "°F"),
  DegreesKelvin		(15, "°K"),
  MilliMetresPerHour	(16, "mm/h"),
  MilliMetresPerDay	(17, "mm/d"),
  MicroSiemens		(18, "µS"),
  MilliSiemens		(19, "mS"),
  Volts				(20, "V"),
  CubicMetresPerSecond(21, "m³/s"),
  UnitsMegaLitresPerDay (22, "ML/d"),
  UnitsUSGallonsPerMinute (23, "US gal/min"),
  UnitsUSGallonsPerHour	(24, "US gal/h"),
  UnitsUSMegaGallonsPerDay(25, "US Mgal/d"),
  UnitsUSGallons			(26, "US gal"),
  //UnitsMegaLitres			(27),
  //UnitsKiloPascal			(28),
  UnitsPoundsPerSquareInch(29, "psi"),
  Unitless				(30, ""),
  Centimetres				(31, "cm"),
  OnOff					(32, ""),
  MilliAmps				(33, "mA"),
  UnitsUSGallonsPerSecond (34, "UA gal/s"),
  UnitsUSGallonsPerDay    (35, "US gal/s"),
  CubicFeetPerSecond      (36, "f³/s"),
  CubicFeetPerMinute      (37, "f³/min"),
  CubicFeetPerHour        (38, "f³/h"),
  CubicFeetPerDay         (39, "f³/d"),
  CubicFeet               (40, "f³"),
  UnitsUSMegaGallons      (41, "US Mgal"),
  Feet                    (42, "f"),
  Inches                  (43, "in"),
  MilliMetresPerSecond    (44, "mm/s"),
  MetresPerSecond         (45, "m/s"),
  FeetPerSecond           (46, "f/s"),
  InchesPerSecond         (47, "in/s"),
  LitresPerMinute         (48, "l/min"),
  KilowattHour            (49, "kWh"),
  Kilowatt                (50, "kW"),
  // Always make 'Unknown' last
  Unknown (-1, "");
	
	public final int index;
	public final String suffix;
	
	Unit(int index, String suffix){
		this.index = index;
		this.suffix = suffix;
	}
	
	public static Unit fromInteger(int index){
		for(Unit u : Unit.values()){
			if(u.index == index) return u;
		}
		
		return Unit.Unknown;
	}
}