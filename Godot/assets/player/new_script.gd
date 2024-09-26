
# Declare member variables
var speed = 200.0
var velocity = Vector2()
var animation_player
var movement = Vector2()



func _process(delta):
	print('hello')
	if Input.is_action_pressed("left"):
		movement.x -= 1
	
	if Input.is_action_pressed("right"):
		movement.x += 1
		velocity = movement.normalized() * speed

	# Move the player using move_and_slide

	# Play the idle animation if not moving
	if not is_moving():
		animation_player.play("idle")

func is_moving():
	return abs(velocity.x) > 0.1 or abs(velocity.y) > 0.1
