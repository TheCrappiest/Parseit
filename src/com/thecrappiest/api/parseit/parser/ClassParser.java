package com.thecrappiest.api.parseit.parser;

import java.io.File;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeTransform;
import java.lang.classfile.MethodTransform;
import java.lang.classfile.TypeKind;
import java.lang.classfile.instruction.ConstantInstruction;
import java.lang.constant.ConstantDescs;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thecrappiest.api.parseit.Parseit;
import com.thecrappiest.api.parseit.util.FileMethods;

public class ClassParser implements Parseit {

    private File toParse;
    private Map<String, String> placeholders = new HashMap<>();

    public ClassParser(File file) {
        toParse = file;
    }

    @Override
    public void parse() {
        parse(toParse);
    }

    @Override
    public void parse(File file) {
        ClassModel unparsed = ClassFile.of().parse(FileMethods.asBytes(toParse));

        ClassFile blank = ClassFile.of();
        CodeTransform trans = (builder, element) -> {
            if (element instanceof ConstantInstruction constant && constant.typeKind() == TypeKind.ReferenceType
                    && !constant.constantValue().equals(ConstantDescs.NULL)) {
                String modify = constant.constantValue().toString();
                for (Entry<String, String> placeholder : placeholders.entrySet())
                    modify = modify.replace(placeholder.getKey(), placeholder.getValue());
                builder.ldc(modify);
            } else
                builder.with(element);
        };

        byte[] newBytes = blank.transform(unparsed,
                ClassTransform.transformingMethods(MethodTransform.transformingCode(trans)));
        FileMethods.writeToFile(file, newBytes);
    }

    @Override
    public Parseit setPlaceholders(Map<String, String> map) {
        placeholders = map;
        return this;
    }

}