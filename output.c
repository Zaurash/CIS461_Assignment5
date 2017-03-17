#include <stdio.h>
#include "builtins/runtime/Builtins.h"



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


struct class_bar_struct;
typedef struct class_bar_struct* class_bar

typedef struct obj_bar_struct {
  class_bar clazz;
  obj_String var;

} * obj_bar;

struct class_bar_struct  the_class_bar_struct;

struct class_bar_struct {
  /* Method table */
  obj_bar (*constructor) (obj_String);
  obj_String (*STRING) (obj_Obj);
  obj_Obj (*PRINT) (obj_Obj);
  obj_Boolean (*EQUALS) (obj_Obj, obj_Obj);
  obj_String (*set_a) (obj_String);
};

extern class_bar the_class_bar

obj_bar new_bar(obj_String var) {
  obj_bar new_thing = (obj_)bar
   malloc(sizeof(struct obj_)bar_struct));
  new_thing->clazz = the_class_bar;
  new_thing->var = var;
  return new_thing;
}


void generated_main();

int main(int argc, char** argv) {
	printf("=== BEGINNING EXECUTION ===\n");
	generated_main();
	printf("=== FINISHED EXECUTION === \n");
}

void generated_main() {

obj_Int tmp_1;
tmp_1 = 4;

obj_Int tmp_2;
tmp_2 = 5;

obj_Obj tmp_3;
tmp_3 = temp;

foo(2, 3);
foo(tmp_1, tmp_2);
x.PLUS(tmp_2);

}
