
Const SCREEN_WIDTH = 1024
Const SCREEN_HEIGHT = 768
Graphics SCREEN_WIDTH, SCREEN_HEIGHT, 16, 2
AppTitle "Movement in four (to eight) directions"
SetBuffer BackBuffer()
FPS = 60
frame_timer = CreateTimer(FPS)


img_bg = LoadImage("img/background-black.png")
ResizeImage img_bg, SCREEN_WIDTH, SCREEN_HEIGHT
img_tile = LoadImage("img/ground_tile.png")


img_player = LoadAnimImage("img/commander_shep_spritesheet.png", 50, 85, 0, 16)

MidHandle img_player
MaskImage img_player, 255, 0, 220





Dim map_list(23, 31) ; 24 x 32 tile matrix
Type TileObject
	Field image
	Field x, y
End Type
TILE_X_NUM = 32
TILE_Y_NUM = 24
TILE_WIDTH = Ceil(SCREEN_WIDTH / TILE_X_NUM)
TILE_HEIGHT = Ceil(SCREEN_HEIGHT / TILE_Y_NUM)
ResizeImage img_tile, TILE_WIDTH, TILE_HEIGHT

setup_tile_grid(img_tile, TILE_X_NUM, TILE_Y_NUM, TILE_WIDTH, TILE_HEIGHT)


Type PlayerObject
	Field image
	Field x#, y#
	Field vector2#[1]
	Field delta_x#, delta_y#
	Field b_idle
	Field anim_interval
	Field anim_update_time
	Field sprite_dir
	Field frame_index
	Field horiz_frame_num
	Field speed
End Type

Const D_DOWN = 0
Const D_LEFT = 4
Const D_RIGHT = 8
Const D_UP = 12


player.PlayerObject = New PlayerObject
player\image = img_player
player\x = SCREEN_WIDTH / 2
player\y = SCREEN_HEIGHT / 2
player\sprite_dir = D_DOWN
player\b_idle = True
player\anim_update_time = MilliSecs()
player\anim_interval = 120
player\frame_index = 0
player\horiz_frame_num = 4
player\speed = 4
player\vector2[0] = 0
player\vector2[1] = 0

Const SCAN_W = 17
Const SCAN_S = 31
Const SCAN_A = 30
Const SCAN_D = 32
Const SCAN_ESC = 1


frame_start_time# = MilliSecs()
frames_per_sec# = 0
run = True
While run
	Cls
	WaitTimer(frame_timer)
	If KeyHit(SCAN_ESC) Then
		run = False
	EndIf
	
	
	draw_bg(img_bg)
	draw_tiles()
	
	get_player_input(player)
	normalize_vector2(player)
	move_player(player)
	update_player(player)
	
	frame_end_time# = MilliSecs()
	seconds_between_frames# = (frame_end_time - frame_start_time) / 1000
	frame_start_time = frame_end_time
	frames_per_sec = 1/seconds_between_frames
	Locate 10, 10
	Print "FPS: " + frames_per_sec
	Flip
Wend
WaitKey
End



Function get_player_input(p.PlayerObject)
	p\b_idle = True
	p\vector2[1] = 0
	p\vector2[0] = 0
	If KeyDown(SCAN_W) Then 
		p\b_idle = False
		p\vector2[1] = -1
		p\sprite_dir = D_UP
	ElseIf KeyDown(SCAN_S) Then
		p\b_idle = False
		p\vector2[1] = 1
		p\sprite_dir = D_DOWN
	EndIf
	
	If KeyDown(SCAN_A) Then
		p\b_idle = False
		p\vector2[0] = -1
		p\sprite_dir = D_LEFT
	ElseIf KeyDown(SCAN_D) Then
		p\b_idle = False
		p\vector2[0] = 1
		p\sprite_dir = D_RIGHT
	EndIf
End Function

Function normalize_vector2(p.PlayerObject)
	If p\vector2[0] <> 0 And p\vector2[1] <> 0 Then
		magnitude# = Sqr((p\vector2[0]^2) + (p\vector2[1]^2))
		p\vector2[0] = p\vector2[0] / magnitude
		p\vector2[1] = p\vector2[1] / magnitude
	EndIf
End Function

Function move_player(p.PlayerObject)
	p\delta_x = p\vector2[0] * p\speed
	p\delta_y = p\vector2[1] * p\speed
	
	p\x = p\x + p\delta_x
	p\y = p\y + p\delta_y
End Function

Function update_player(p.PlayerObject)
	If Not p\b_idle Then
		current_time = MilliSecs()
		If current_time - p\anim_update_time > p\anim_interval Then
			p\frame_index = p\frame_index + 1
			p\anim_update_time = MilliSecs()
		EndIf
	Else
		p\frame_index = 0
	EndIf
	
	If p\frame_index >= p\horiz_frame_num Then
		p\frame_index = 0
	EndIf
	
	DrawImage p\image, p\x, p\y, p\frame_index + p\sprite_dir
End Function









.map_data
Data 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1                                                                                              
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 
Data 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1

Function setup_tile_grid(img, tile_num_x, tile_num_y, tile_width, tile_height)
	Restore map_data
	For row = 0 To tile_num_y - 1
		For col = 0 To tile_num_x - 1
			Read map_list(row, col)
		Next
	Next
	
	For row = 0 To tile_num_y -1
		For col = 0 To tile_num_x - 1
			If map_list(row, col) = 1 Then
				t.TileObject = New TileObject
				t\image = img
				t\x = col * tile_width
				t\y = row * tile_height
			EndIf
		Next
	Next
End Function

Function draw_tiles()
	For t.TileObject = Each TileObject
		DrawImage t\image, t\x, t\y
	Next
End Function

Function draw_bg(img)
	DrawImage img, 0, 0
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D