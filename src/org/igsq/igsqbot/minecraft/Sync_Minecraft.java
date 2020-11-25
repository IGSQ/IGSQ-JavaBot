package org.igsq.igsqbot.minecraft;

import java.util.Hashtable;
import java.util.Map;

import org.igsq.igsqbot.Common;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Sync_Minecraft 
{
	private static final Map<String, String> ranks = new Hashtable<String, String>();
	
	public static void sync()
	{
		for(Member selectedMember : Common.jda.getGuildById("769356660799176754").loadMembers().get())
		{
			if(!selectedMember.getUser().isBot())
			{
				String nickname = selectedMember.getEffectiveName();
				String id = selectedMember.getId();
				String rank = fetchRank(selectedMember);
				boolean supporter = hasRole("675828125493624832", selectedMember);
				boolean developer = hasRole("751892688068149369", selectedMember) || hasRole("599515693951614976", selectedMember);
				boolean founder = hasRole("654577195980685324", selectedMember);
				boolean retired = hasRole("768499465552134195", selectedMember);
				boolean nitroboost = hasRole("639356842547347456", selectedMember);
			}
		}
	}
	
	private static boolean hasRole(String roleID, Member member)
	{
		for(Role selectedRole : member.getRoles())
		{
			if(selectedRole.getId().equals(roleID))
			{
				return true;
			}
		}
		return false;
	}
	
	private static String fetchRank(Member member)
	{
		if(ranks.isEmpty())
		{
			setRanks();
		}
		
		for(Role selectedRole : member.getRoles())
		{
			for(String selectedRank : ranks.keySet())
			{
				if(selectedRole.getId().equals(selectedRank))
				{
					return ranks.get(selectedRank);
				}
			}
		}
		return null;
	}
	
	private static void setRanks()
	{
		ranks.put("558697600375848970", "default");
		
		ranks.put("701141681998790737", "rising");
		ranks.put("742059848983904387", "flying");
		ranks.put("701141500029042718", "soaring");
		
		ranks.put("664159369415622677", "epic");
		ranks.put("664159373421182996", "epic2");
		ranks.put("664159376914776104", "epic3");
		
		ranks.put("664148646207684610", "elite");
		ranks.put("664148507489468436", "elite2");
		ranks.put("664148336516923395", "elite3");
		
		ranks.put("701141500029042718", "mod");
		ranks.put("742059848983904387", "mod2");
		ranks.put("701141681998790737", "mod3");
		
		ranks.put("558697600375848970", "council");
	}
}
