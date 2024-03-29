/**
 *  Color Picker by Juan Martín
 *  Copyright (C) 2010 nauj27.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.nauj27.android.colorpicker.ral;

/**
 * @author nauj27
 *
 */
public class RalSystem {
	public static int[] code = {
		1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1011, 1012, 
		1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1023,	
		1024, 1026, 1027, 1028,	1032, 1033, 1034, 1035, 1036, 1037, 
		2000, 2001, 2002, 2003, 2004, 2005,	2007, 2008, 2009, 2010, 
		2011, 2012,	2013, 3000, 3001, 3002, 3003, 3004,	3005, 3007, 
		3009, 3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 3020,
		3022, 3024, 3026, 3027,	3028, 3031, 3032, 3033, 4001, 4002, 
		4003, 4004,	4005, 4006, 4007, 4008, 4009, 4010, 4011, 4012, 
		5000, 5001, 5002, 5003, 5004, 5005, 5007, 5008, 5009, 5010, 
		5011, 5012, 5013, 5014, 5015, 5017, 5018, 5019, 5020, 5021, 
		5022, 5023, 5024, 5025, 5026, 6000, 6001, 6002, 6003, 6004, 
		6005, 6006, 6007, 6008, 6009, 6010, 6011, 6012, 6013, 6014, 
		6015, 6016, 6017, 6018, 6019, 6020, 6021, 6022, 6024, 6025, 
		6026, 6027, 6028, 6029, 6032, 6033, 6034, 6035, 6036, 6037, 
		6038, 7000,	7001, 7002, 7003, 7004, 7005, 7006, 7008, 7009, 
		7010, 7011,	7012, 7013, 7015, 7016, 7021, 7022, 7023, 7024, 
		7026, 7030,	7031, 7032, 7033, 7034, 7035, 7036, 7037, 7038, 
		7039, 7040,	7042, 7043, 7044, 7045, 7046, 7047, 7048, 8000, 
		8001, 8002,	8003, 8004, 8007, 8008, 8011, 8012, 8014, 8015, 
		8016, 8017,	8019, 8022, 8023, 8024, 8025, 8028, 8029, 9001, 
		9002, 9003,	9004, 9005, 9006, 9007, 9010, 9011, 9016, 9017, 
		9018, 9022,	9023
	};
	
	public static int[] red = {
		190, 194, 198, 229, 205, 169, 228, 220, 138, 199, 
		234, 225, 230, 237, 245, 248, 158, 153, 243, 250, 
		174, 255, 157, 244, 214, 243, 239, 106,	112, 243, 
		237, 201, 203, 255, 244, 255, 255, 247, 245, 216, 
		236, 235, 195, 175, 165, 162, 155, 117,  94,  65, 
		100, 120, 193, 161, 211, 234, 179, 230, 213, 204, 
		217, 248, 254, 197, 255, 179, 114, 180, 222, 146,
		222, 110, 108, 160,  74, 146, 164, 215, 134, 108,
		 42,  31,  32,  29,  32,  30,  62,  38,   2,  14,
		 35,  59,  37,  96,  34,   6,  63,  27,  29,  37,  
		 37,  73,  93,  42,  16,  49,  40,  45,  66,  31,  
		 47,  62,  52,  57,  49,  53,  88,  52, 108,  71,  
		 59,  30,  76,  87, 189,  46, 137,  37,  48,  61,  
		 01, 132,  44,  32,  49,  73, 127,  28,  22,   0,
		  0, 120, 138, 126, 108, 150, 100, 109, 106,  77,
		 76,  67,  78,  70,  67,  41,  35,  51, 104,  71, 
		 47, 139,  71, 184, 125, 143, 215, 127, 125, 195, 
		108, 157, 141,  78, 202, 144, 130, 208, 137, 130,
		149, 108, 115, 142,  89, 111,  91,  89,  56,  99,
		 76,  69,  64,  33, 166, 121, 117,  78, 118, 250, 
		231, 244,  40,  10, 165, 143, 255,  28, 246,  30, 
		215, 156, 130
	};

	public static int[] green = {
 		189, 176, 166, 190, 164, 131, 160, 156, 102, 180, 
 		230, 204, 214, 255,	208, 243, 151, 153, 218, 210,
 		160, 255, 145, 169, 174, 165, 169,  93,	 83, 159, 
 		118,  60,  40, 117,  70,  35, 164,  94,  64,  75, 
 		124, 106,  88,  43,  32,  35,  17,  21,  33,  34, 
 		 36,  31, 135,  35, 110, 137,  40,  50,  48,   6,
 		 80,   0,  00,  29,	  0,  36,  20,  76,  76,  43,
 		 76,  28,  70,  52,  25,  78, 125,  45, 115, 104, 
 		 46,  52,  33,  30,  33,  45,  95,  37,  86,  41,
 		 26, 131,  41, 111, 113,  57, 136,  85,  51, 109,
 		 40, 103, 155, 100,  44, 102, 114,  87,  70,  58,
 		 69,  59,  59,  53,  55, 104, 114,	62, 113,  64,
 		 60,  89, 145, 166, 236,  58, 172,  34, 132, 100,
 		 93, 195,  85,  96, 127, 126, 181,  84,  53, 255,
 		248, 133, 149, 123, 112, 153, 107, 101,  95,  86,
 		 81,  75,  87,  69,  71,  49,  40,  47, 108,  74,
 		 53, 140,  75, 183, 132, 139, 215, 118, 127, 195,
 		105, 161, 148,  84, 196, 144, 137, 208, 129, 108, 
 		 95,  59,  66,  64,  53,  79,  58,  35,  44,  58,
 		 47,  50,  58,  33,  94,  85,  92,  59,  60, 244,
 		235, 244,  40,  10, 165, 143, 255,  28, 246,  30,
 		215, 156, 130
	};
	
	public static int[] blue = {
		127, 120, 100,   1,  52,   7,  16,   0,  66,  70, 
		202,  79, 144,  33,  51,  53, 100,  80,  11,   1,  
		 75,   0,   1,   0,   1,   5,  74,  77,  53,  24, 
		 14,  32,  33,  20,  17,   1,  32,  37,  33,  32, 
		 38,  14,  49,  30,  25,  29,  30,  30,  41,  39,
		 36,  25, 107,  18, 112, 154,  33,  68,  50,   5, 
		 48,   0,   0,  52,   0,  40,  34,  67, 138,  62,
	    138,  52, 117, 114,  44, 125, 144, 109, 161, 129, 
	     75,  56,  79,  51,  79, 110, 138,  45, 105,  75,
	     36, 189,  74, 140, 179, 113, 143, 131,  74, 123,
	     80, 141, 155, 120,  84,  80,  51,  44,  50,  61,
	     56,  50,  41,  42,  43,  45,  70,  64,  86,  46, 
	     54,  69,  65,  57, 182,  35, 118,  27,  70,  45,
	     82, 190,  69,  61,  67, 118, 181,  45,  55,   0,
	      0, 139, 151,  82,  89, 146,  99,  82,  49,  69, 
	     74,  77,  84,  49,  80,  51,  43,  44,  94,  81,
	     59, 122,  78, 153, 113, 102, 215, 121, 120, 195,
	     96, 170, 141,  82, 176, 144, 143, 208, 118,  52,
	     32,  42,  34,  42,  31,  40,  41,  33,  30,  52,
	     39,  46,  58,  33,  46,  61,  72,  49,  40, 227,
	     218, 244,  40, 10, 165, 143, 255,  28, 246,  30,
	     215, 156, 130
	};
	
	public static int[] hex = {
		0xBEBD7F, 0xC2B078, 0xC6A664, 0xE5BE01, 0xCDA434, 
		0xA98307, 0xE4A010, 0xDC9D00, 0x8A6642, 0xC7B446,
		0xEAE6CA, 0xE1CC4F,	0xE6D690, 0xEDFF21, 0xF5D033,
		0xF8F32B, 0x9E9764, 0x999950, 0xF3DA0B, 0xFAD201,
		0xAEA04B, 0xFFFF00, 0x9D9101, 0xF4A900,	0xD6AE01,
		0xF3A505, 0xEFA94A, 0x6A5D4D, 0x705335, 0xF39F18, 
		0xED760E, 0xC93C20, 0xCB2821, 0xFF7514, 0xF44611,
		0xFF2301, 0xFFA420, 0xF75E25, 0xF54021, 0xD84B20,
		0xEC7C26, 0xE55137, 0xC35831, 0xAF2B1E, 0xA52019, 
		0xA2231D, 0x9B111E, 0x75151E, 0x5E2129, 0x412227, 
		0x642424, 0x781F19, 0xC1876B, 0xA12312, 0xD36E70, 
		0xEA899A, 0xB32821, 0xE63244, 0xD53032, 0xCC0605,
		0xD95030, 0xF80000, 0xFE0000, 0xC51D34, 0xFF0000,
		0xB32428, 0x721422, 0xB44C43, 0x6D3F5B, 0x922B3E,
		0xDE4C8A, 0x641C34,	0x6C4675, 0xA03472, 0x4A192C,
		0x924E7D, 0xA18594, 0xCF3476, 0x8673A1, 0x6C6874, 
		0x354D73, 0x1F3438, 0x20214F, 0x1D1E33, 0x18171C,
		0x1E2460, 0x3E5F8A, 0x26252D, 0x025669, 0x0E294B, 
		0x231A24, 0x3B83BD, 0x1E213D, 0x606E8C, 0x2271B3,
		0x063971, 0x3F888F,	0x1B5583, 0x1D334A, 0x256D7B,
		0x252850, 0x49678D, 0x5D9B9B, 0x2A6478, 0x102C54, 
		0x316650, 0x287233, 0x2D572C, 0x424632,	0x1F3A3D,
		0x2F4538, 0x3E3B32, 0x343B29, 0x39352A, 0x31372B,
		0x35682D, 0x587246, 0x343E40, 0x6C7156, 0x47402E,
		0x3B3C36, 0x1E5945, 0x4C9141, 0x57A639, 0xBDECB6,
		0x2E3A23, 0x89AC76,	0x25221B, 0x308446, 0x3D642D,
		0x015D52, 0x84C3BE, 0x2C5545, 0x20603D, 0x317F43,
		0x497E76, 0x7FB5B5, 0x1C542D, 0x193737, 0x00FF00,
		0x00F800, 0x78858B,	0x8A9597, 0x7E7B52, 0x6C7059,
		0x969992, 0x646B63,	0x6D6552, 0x6A5F31, 0x4D5645, 
		0x4C514A, 0x434B4D, 0x4E5754, 0x464531, 0x434750,
		0x293133, 0x23282B, 0x332F2C, 0x686C5E,	0x474A51,
		0x2F353B, 0x8B8C7A, 0x474B4E, 0xB8B799, 0x7D8471,
		0x8F8B66, 0xD7D7D7, 0x7F7679, 0x7D7F7D, 0xB5B8B1,
		0x6C6960, 0x9DA1AA, 0x8D948D, 0x4E5452, 0xCAC4B0,
		0x909090, 0x82898F,	0xD0D0D0, 0x898176, 0x826C34,
		0x955F20, 0x6C3B2A, 0x734222, 0x8E402A, 0x59351F,
		0x6F4F28, 0x5B3A29, 0x592321, 0x382C1E,	0x633A34,
		0x4C2F27, 0x45322E, 0x403A3A, 0x212121, 0xA65E2E,
		0x79553D, 0x755C48, 0x4E3B31, 0x763C28, 0xFDF4E3,
		0xE7EBDA, 0xF4F4F4, 0x282828, 0x0A0A0A, 0xA5A5A5,
		0x8F8F8F, 0xFFFFFF,	0x1C1C1C, 0xF6F6F6, 0x1E1E1E,
		0xD7D7D7, 0x9C9C9C, 0x828282
	};
	
}
