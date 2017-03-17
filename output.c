#include <stdio.h>
#include "builtins/runtime/Builtins.h"



//Defining class foo
struct class_foo_struct;
typedef struct class_foo_struct* class_foo

typedef struct obj_foo_struct {
  class_foo clazz;
  obj_Int x;
  obj_Int y;

} * obj_foo;

struct class_foo_struct  the_class_foo_struct;

struct class_foo_struct {
  /* Method table */
  obj_foo (*constructor) (obj_Int, obj_Int);
  obj_String (*STRING) (obj_Obj);
  obj_Obj (*PRINT) (obj_Obj);
  obj_Boolean (*EQUALS) (obj_Obj, obj_Obj);
  obj_Int (*PLUS) (obj_Int, obj_Int);
};

extern class_foo the_class_foo

obj_foo new_foo(obj_Int x, obj_Int y) {
  obj_foo new_thing = (obj_)foo
   malloc(sizeof(struct obj_)foo_struct));
  new_thing->clazz = the_class_foo;
  new_thing->x = x;
  new_thing->y = y;
  return new_thing;
}
obj_Int foo_method_PLUS(obj_Int, obj_Int) {
return ;

}
struct  class_foo_struct  the class_foo_struct = {
  new_foo,
  Obj_method_STRING,
  Obj_method_PRINT,
  Obj_method_EQUALS,
  foo_method_PLUS
};
class_foothe_class_foo = &the_class_foo_struct;


//Defining class bar
struct class_bar_struct;
typedef struct class_bar_struct* class_bar

typedef struct obj_bar_struct {
  class_bar clazz;

} * obj_bar;

struct class_bar_struct  the_class_bar_struct;

struct class_bar_struct {
  /* Method table */
  obj_bar (*constructor) (void);
  obj_String (*STRING) (obj_Obj);
  obj_Obj (*PRINT) (obj_Obj);
  obj_Boolean (*EQUALS) (obj_Obj, obj_Obj);

extern class_bar the_class_bar

obj_bar new_bar(void) {
  obj_bar new_thing = (obj_)bar
   malloc(sizeof(struct obj_)bar_struct));
  new_thing->clazz = the_class_bar;
  return new_thing;
}
struct  class_bar_struct  the class_bar_struct = {
  new_bar,
  Obj_method_STRING,
  Obj_method_PRINT,
  Obj_method_EQUALS};
class_barthe_class_bar = &the_class_bar_struct;


void generated_main();

int main(int argc, char** argv) {
	printf("=== BEGINNING EXECUTION ===\n");
	generated_main();
	printf("=== FINISHED EXECUTION === \n");
}

void generated_main() {

obj_Int tmp_1;
tmp_1 = 2;

obj_Int tmp_2;
tmp_2 = 2;

obj_String tmp_3;
tmp_3 = "hi";

if (false) {

tmp_1 = 2;

return ;

}

else{

}

while(){
if (false) {

tmp_2 = tmp_1;

}

else{

tmp_1 = tmp_2;

}

}

foo(1, 2);
foo(tmp_1, tmp_2);

}
