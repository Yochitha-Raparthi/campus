package com.campuspulse.service.impl;
import org.springframework.stereotype.Service;import com.campuspulse.service.ClassifierService;import com.campuspulse.dto.response.Responses;import com.campuspulse.enums.*;import java.util.*;
@Service public class ClassifierServiceImpl implements ClassifierService{
public Responses.Analysis analyze(String title,String description){String s=(title+" "+description).toLowerCase(Locale.ROOT);Category c=Category.OTHER;String why="General campus issue";
if(has(s,"wifi","wi-fi","internet","network","router")){c=Category.WIFI;why="Network connectivity terms detected";}
else if(has(s,"electric","power","light","socket","projector","fan")){c=Category.ELECTRICAL;why="Electrical or equipment terms detected";}
else if(has(s,"water","tap","leak","drain","toilet")){c=Category.WATER;why="Water or plumbing terms detected";}
else if(has(s,"dirty","trash","garbage","clean","waste")){c=Category.CLEANLINESS;why="Hygiene or cleaning terms detected";}
else if(has(s,"harass","theft","unsafe","security","fight")){c=Category.SECURITY;why="Safety or security terms detected";}
else if(has(s,"broken","chair","desk","door","wall","roof")){c=Category.INFRASTRUCTURE;why="Facilities maintenance terms detected";}
Priority p=has(s,"fire","smoke","injury","electrocution","violence","gas leak")?Priority.CRITICAL:has(s,"urgent","danger","unsafe","flood","no water","sparks")?Priority.HIGH:has(s,"minor","cosmetic","suggestion")?Priority.LOW:Priority.MEDIUM;
return new Responses.Analysis(c,p,why+"; priority suggested from keyword-based rules (not a trained ML model).");}
private boolean has(String s,String... words){for(String w:words)if(s.contains(w))return true;return false;}
}
